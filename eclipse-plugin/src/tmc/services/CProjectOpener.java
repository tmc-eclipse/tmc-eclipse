package tmc.services;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.IPDOMManager;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.internal.core.pdom.indexer.IndexerPreferences;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.make.core.makefile.IMakefile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;

@SuppressWarnings("restriction")
public class CProjectOpener {
    private String projectPath;
    private String projectName;

    public CProjectOpener(String path, String name) {
        projectPath = path;
        projectName = name;
    }

    public void importAndOpen() throws URISyntaxException, OperationCanceledException, CoreException {
        // IWorkspace workspace = ResourcesPlugin.getWorkspace();
        // IWorkspaceRoot root = workspace.getRoot();
        //
        // IProject project = root.getProject(projectName);
        // IndexerPreferences.set(project, IndexerPreferences.KEY_INDEXER_ID,
        // IPDOMManager.ID_NO_INDEXER);
        //
        // IProjectDescription description =
        // workspace.newProjectDescription(projectName);
        //
        // description.setLocationURI(new URI(projectPath));
        //
        // // project = CCorePlugin.getDefault().createCDTProject(description,
        // // project, new NullProgressMonitor());
        // project = CCorePlugin.getDefault().createCProject(description,
        // project, new NullProgressMonitor(), projectName);
        // project.create(new NullProgressMonitor());
        //
        // project.open(new NullProgressMonitor());
        //

        try {
            IWorkspaceRoot theRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject theProject = theRoot.getProject(projectName);
            IProjectDescription theDesc = theProject.getWorkspace().newProjectDescription(projectName);
            theDesc.setLocation(new Path(projectPath));
            theProject.create(theDesc, new NullProgressMonitor());
            if (theProject.exists()) {
                theProject.open(new NullProgressMonitor());
            }
        } catch (CoreException err) {
            err.printStackTrace();
        }
    }
}
