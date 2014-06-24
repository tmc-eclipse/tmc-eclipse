package tmc.eclipse.openers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.IMavenProjectImportResult;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;

import tmc.eclipse.util.TMCProjectNature;

public class MavenProjectOpener {
    private IWorkspace workspace;
    private File pomFile;
    protected static final IProgressMonitor monitor = new NullProgressMonitor();

    public MavenProjectOpener(String pathToPom) {

        workspace = ResourcesPlugin.getWorkspace();

        pomFile = new File(pathToPom);
    }

    public IProject importAndOpen() throws CoreException, IOException {
        pomFile.getParentFile().getCanonicalPath();

        IProject project = importProject();

        return project;
    }

    public IProject importProject() throws CoreException, IOException {
        final ArrayList<MavenProjectInfo> projectInfos = initializeProjectinfoList();
        final ProjectImportConfiguration importConfiguration = new ProjectImportConfiguration();
        final ArrayList<IMavenProjectImportResult> importResults = new ArrayList<IMavenProjectImportResult>();

        workspace.run(new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {

                importResults.addAll(MavenPlugin.getProjectConfigurationManager().importProjects(projectInfos,
                        importConfiguration, monitor));

            }
        }, MavenPlugin.getProjectConfigurationManager().getRule(), IWorkspace.AVOID_UPDATE, monitor);

        IProject project = importResults.get(0).getProject();

        return project;

    }

    private ArrayList<MavenProjectInfo> initializeProjectinfoList() throws CoreException, IOException {

        MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
        final ArrayList<MavenProjectInfo> projectInfos = new ArrayList<MavenProjectInfo>();
        Model model = mavenModelManager.readMavenModel(this.pomFile);
        MavenProjectInfo projectInfo = new MavenProjectInfo(pomFile.getName(), pomFile, model, null);
        setBasedirRename(projectInfo);
        projectInfos.add(projectInfo);
        return projectInfos;

    }

    private void setBasedirRename(MavenProjectInfo projectInfo) throws IOException {
        File workspaceRoot = workspace.getRoot().getLocation().toFile();
        File basedir = projectInfo.getPomFile().getParentFile().getCanonicalFile();
        projectInfo.setBasedirRename(basedir.getParentFile().equals(workspaceRoot) ? MavenProjectInfo.RENAME_REQUIRED
                : MavenProjectInfo.RENAME_NO);
    }

}
