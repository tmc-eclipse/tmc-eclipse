package tmc.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.core.dom.IPDOMManager;
import org.eclipse.cdt.internal.core.pdom.indexer.IndexerPreferences;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.make.core.MakeProjectNature;
import org.eclipse.cdt.make.core.scannerconfig.ScannerConfigNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;

@SuppressWarnings("restriction")
public class CProjectOpener {
    private String projectPath;
    private String projectName;

    public CProjectOpener(String path, String name) {
        projectPath = path;
        projectName = name;
    }

    @SuppressWarnings("deprecation")
    public void importAndOpen() throws URISyntaxException, OperationCanceledException, CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();

        IProject project = root.getProject(projectName);
        IndexerPreferences.set(project, IndexerPreferences.KEY_INDEXER_ID, IPDOMManager.ID_NO_INDEXER);

        IProjectDescription description = workspace.newProjectDescription(projectName);

        description.setLocationURI(new URI(projectPath));

        project = CCorePlugin.getDefault().createCProject(description, project, new NullProgressMonitor(),
                MakeCorePlugin.MAKE_PROJECT_ID);

        if (!project.hasNature(CProjectNature.C_NATURE_ID)) {
            CProjectNature.addCNature(project, monitor);
        }

        if (!project.hasNature(MakeProjectNature.NATURE_ID)) {
            MakeProjectNature.addNature(project, new SubProgressMonitor(monitor, 1));
        }

        ScannerConfigNature.addScannerConfigNature(project);

        CCorePlugin.getDefault().mapCProjectOwner(project, projectName, true);

        project.open(new NullProgressMonitor());

    }
}
