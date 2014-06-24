package tmc.eclipse.util;

import java.awt.Desktop;
import java.net.URI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import tmc.eclipse.activator.CoreInitializer;
import tmc.eclipse.handlers.listeners.SelectionListener;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;

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

    public boolean setupSelectionListener(final SelectionListener listener) {

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

    /**
     * Returns an usable shell via some magic. Shell is *most likely* the main
     * Eclipse shell, but this is not guaranteed.
     */
    public Shell getUsableShell() {
        /*
         * Default display seems to always have AT LEAST three shells:
         * 
         * Shell #0 = "PartRenderingEngine's limbo". Intended for elements that
         * should not be part of current view.
         * 
         * Shell #1 = "Resource - Eclipse Platform". Changes (or atleast the
         * name does) based on selected MAIN UI element. Opening a dialog
         * doesn't change this shell.
         * 
         * Shell #2 = "Quick Access". Also always present but probably not the
         * one we want to semantically use.
         * 
         * Out of these three, Shell #1 seems like the best bet, therefore we
         * should use it.
         * 
         * As a fall back, if the shell doesn't for some reason exist, we'll use
         * Shell #0.
         * 
         * If THAT doesn't exist, we try to get the active shell. This is very
         * unlikely to exist in such a case (f.ex. it doesn't exist if the focus
         * is on some other window than the Eclipse IDE).
         * 
         * If EVERYTHING fails, we just create a new shell.
         */
        Shell shell = null;
        if (Display.getDefault().getShells().length > 1) {
            shell = Display.getDefault().getShells()[1];
            if (shell == null && Display.getDefault().getShells().length > 0) {
                shell = Display.getDefault().getShells()[0];
            }
            if (shell == null) {
                shell = Display.getDefault().getActiveShell();
            }
        }

        if (shell == null) {
            shell = new Shell();
        }

        return shell;
    }
}
