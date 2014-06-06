package tmc.handlers.listeners;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

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
                }
            }
        }
    }

}
