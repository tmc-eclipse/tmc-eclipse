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
        System.out.println("MPO constructor");
        workspace = ResourcesPlugin.getWorkspace();
        resolverConfiguration = new ResolverConfiguration();
        pomFile = new File(pathToPom);
        System.out.println("Constructor finished");
    }

    public void importAndOpen() throws CoreException, IOException {
        System.out.println("import and open called");
        try {
            baseDir = pomFile.getParentFile().getCanonicalPath();
        } catch (IOException e) {
            System.out.println("Failed to parse base directory from pomFile");
            e.printStackTrace();
        }
        System.out.println("basedir set");
        IProject project = importProject();
        System.out.println("project imported");
        if (project != null) {
            project.open(monitor);
            System.out.println("project opened");
        }
    }

    public IProject importProject() throws CoreException, IOException {
        MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
        System.out.println("Mavenmodelmanager generated");
        IWorkspaceRoot root = workspace.getRoot();

        final ArrayList<MavenProjectInfo> projectInfos = new ArrayList<MavenProjectInfo>();
        Model model = mavenModelManager.readMavenModel(this.pomFile);
        System.out.println("pomfile read");
        MavenProjectInfo projectInfo = new MavenProjectInfo(pomFile.getName(), pomFile, model, null);
        System.out.println("projectinfo generated");
        setBasedirRename(projectInfo);
        projectInfos.add(projectInfo);

        final ProjectImportConfiguration importConfiguration = new ProjectImportConfiguration(resolverConfiguration);
        final ArrayList<IMavenProjectImportResult> importResults = new ArrayList<IMavenProjectImportResult>();

        workspace.run(new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {

                importResults.addAll(MavenPlugin.getProjectConfigurationManager().importProjects(projectInfos,
                        importConfiguration, monitor));
                System.out.println("importresults added");

            }
        }, MavenPlugin.getProjectConfigurationManager().getRule(), IWorkspace.AVOID_UPDATE, monitor);

        IProject project = importResults.get(0).getProject();
        System.out.println("project returned");

        return project;

    }

    private void setBasedirRename(MavenProjectInfo projectInfo) throws IOException {
        File workspaceRoot = workspace.getRoot().getLocation().toFile();
        File basedir = projectInfo.getPomFile().getParentFile().getCanonicalFile();
        projectInfo.setBasedirRename(basedir.getParentFile().equals(workspaceRoot) ? MavenProjectInfo.RENAME_REQUIRED
                : MavenProjectInfo.RENAME_NO);
    }

}
