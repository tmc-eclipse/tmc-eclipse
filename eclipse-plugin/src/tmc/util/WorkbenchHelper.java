package tmc.util;

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
        IWorkbench workbench = CoreInitializer.getDefault().getWorkbench();
        if (workbench == null) {
            return false;
        }

        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        if (workbenchWindow == null) {
            return false;
        }

        ISelectionService selectionService = workbenchWindow.getSelectionService();
        if (selectionService == null) {
            return false;
        }

        selectionService.addSelectionListener(listener);
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
            return getProjectBySelection();
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

}
