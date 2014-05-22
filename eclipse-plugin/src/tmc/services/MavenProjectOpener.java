package tmc.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
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
import org.eclipse.m2e.core.project.ResolverConfiguration;

//TODO: Refaktoroi koko paska.
public class MavenProjectOpener {
    private IWorkspace workspace;
    private File pomFile;
    private String baseDir;
    private ResolverConfiguration resolverConfiguration;
    protected static final IProgressMonitor monitor = new NullProgressMonitor();

    public MavenProjectOpener(String pathToPom) {
        workspace = ResourcesPlugin.getWorkspace();
        resolverConfiguration = new ResolverConfiguration();
        pomFile = new File(pathToPom);
    }

    public void importAndOpen() throws CoreException, IOException {
        try {
            baseDir = pomFile.getParentFile().getCanonicalPath();
        } catch (IOException e) {
            System.out.println("Failed to parse base directory from pomFile");
            e.printStackTrace();
        }

        IProject project = importProject();

        if (project != null) {
            project.open(monitor);
        }
    }

    public IProject importProject() throws CoreException, IOException {
        MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
        IWorkspaceRoot root = workspace.getRoot();
        File src = new File(baseDir);
        File dst = new File(root.getLocation().toFile(), src.getName());

        final ArrayList<MavenProjectInfo> projectInfos = new ArrayList<MavenProjectInfo>();
        File pomFile = new File(dst, this.pomFile.getName());
        Model model = mavenModelManager.readMavenModel(pomFile);
        MavenProjectInfo projectInfo = new MavenProjectInfo(pomFile.getName(), pomFile, model, null);
        setBasedirRename(projectInfo);
        projectInfos.add(projectInfo);

        final ProjectImportConfiguration importConfiguration = new ProjectImportConfiguration(resolverConfiguration);
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

    private void setBasedirRename(MavenProjectInfo projectInfo) throws IOException {
        File workspaceRoot = workspace.getRoot().getLocation().toFile();
        File basedir = projectInfo.getPomFile().getParentFile().getCanonicalFile();
        projectInfo.setBasedirRename(basedir.getParentFile().equals(workspaceRoot) ? MavenProjectInfo.RENAME_REQUIRED
                : MavenProjectInfo.RENAME_NO);
    }

}
