package tmc.util;

import java.awt.Desktop;
import java.net.URI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import tmc.activator.CoreInitializer;
import tmc.handlers.listeners.SelectionListener;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;

public class WorkbenchHelper {

    private static final String SOURCE_EDITOR = "CompilationUnitEditor";
    private IWorkbenchPart view;
    private SelectionListener selectionListener;
    private ProjectDAO projectDAO;

    private boolean isListenerSetUp = false;

    public WorkbenchHelper(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
        this.selectionListener = new SelectionListener(projectDAO);
        initialize();
    }

    public void initialize() {
        if (!isListenerSetUp) {
            if (setupSelectionListener(selectionListener)) {
                System.out.println("Toimii");
                isListenerSetUp = true;
            }
        }
    }

    public void updateActiveView() {
        IWorkbench workbench = CoreInitializer.getDefault().getWorkbench();
        if (workbench == null) {
            return;
        }

        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        if (workbenchWindow == null) {
            return;
        }

        IWorkbenchPage activePage = workbenchWindow.getActivePage();
        if (activePage == null) {
            return;
        }

        IWorkbenchPart activePart = activePage.getActivePart();
        if (activePart == null) {
            return;
        }

        this.view = activePart;
    }

    public boolean setupSelectionListener(SelectionListener listener) {

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                IWorkbench workbench = CoreInitializer.getDefault().getWorkbench();

                IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();

                ISelectionService selectionService = workbenchWindow.getSelectionService();

                selectionService.addSelectionListener(listener);

            }
        });

        return true;

    }

    public String getActiveView() {
        if (view == null) {
            return "";
        }

        return view.getClass().getSimpleName();
    }

    public Project getActiveProject() {
        if (getActiveView().equals(SOURCE_EDITOR)) {
            return getProjectByEditor();
        } else {
            Project project = getProjectBySelection();

            if (project == null) {
                System.out.println("Project on null");
                project = getProjectByEditor();
            }

            return project;
        }
    }

    private Project getProjectBySelection() {
        return selectionListener.getSelectedProject();
    }

    private Project getProjectByEditor() {
        try {
            IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput();
            String projectRoot = input.getFile().getProject().getRawLocation().makeAbsolute().toString();
            return projectDAO.getProjectByFile(projectRoot);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean saveOpenFiles() {
        return PlatformUI.getWorkbench().saveAllEditors(true);
    }

    public void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                Core.getErrorHandler().raise("Unable to open the default browser.");
            }
        } else {
            Core.getErrorHandler().raise("Unable to find a default system browser.");
        }

    }
}
