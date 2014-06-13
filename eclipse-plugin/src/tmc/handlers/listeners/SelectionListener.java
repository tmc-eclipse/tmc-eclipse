package tmc.handlers.listeners;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.services.IServiceLocator;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;

public class SelectionListener implements ISelectionListener {

    private ProjectDAO projectDAO;
    private Project project;

    public SelectionListener(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public Project getSelectedProject() {
        return project;
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof TreeSelection) {
            TreeSelection treeSelection = (TreeSelection) selection;
            Object element = treeSelection.getFirstElement();

            if (element instanceof IAdaptable) {
                IAdaptable adaptable = (IAdaptable) element;
                IResource resource = (IResource) adaptable.getAdapter(IResource.class);
                if (resource != null) {
                    IPath path = resource.getLocation();
                    this.project = projectDAO.getProjectByFile(path.toString());
                    updateSubmitButton();
                }
            }
        }
    }

    private void updateSubmitButton() {

        IServiceLocator serviceLocator = PlatformUI.getWorkbench();

        ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);

        try {

            Command command = commandService
                    .getCommand("fi.helsinki.cs.plugins.eclipse.commands.submitButtonStateCommand");

            command.executeWithChecks(new ExecutionEvent());

        } catch (Exception e) {
            Core.getErrorHandler().handleException(e);
        }
    }

}
