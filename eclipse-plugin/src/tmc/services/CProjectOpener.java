package tmc.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.IPDOMManager;
import org.eclipse.cdt.internal.core.pdom.indexer.IndexerPreferences;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

@SuppressWarnings("restriction")
public class CProjectOpener {
    private String projectPath;
    private String projectName;

    public CProjectOpener(String path, String name) {
        projectPath = path;
        projectName = name;
    }

    public void importAndOpen() throws URISyntaxException, OperationCanceledException, CoreException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();

        IProject project = root.getProject(projectName);
        IndexerPreferences.set(project, IndexerPreferences.KEY_INDEXER_ID, IPDOMManager.ID_NO_INDEXER);

        IProjectDescription description = workspace.newProjectDescription(projectName);

        description.setLocationURI(new URI(projectPath));

        project = CCorePlugin.getDefault().createCProject(description, project, new NullProgressMonitor(), projectName);

        project.open(new NullProgressMonitor());

    }
}
