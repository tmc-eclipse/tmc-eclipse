package tmc.services;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.core.dom.IPDOMManager;
import org.eclipse.cdt.internal.core.ConsoleOutputSniffer;
import org.eclipse.cdt.internal.core.pdom.indexer.IndexerPreferences;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.make.core.MakeProjectNature;
import org.eclipse.cdt.make.core.scannerconfig.ScannerConfigNature;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IProjectType;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.core.ManagedCProjectNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;

import fi.helsinki.cs.plugin.tmc.io.FileUtil;

@SuppressWarnings("restriction")
public class CProjectOpener {
	private String projectPath;
	private String projectName;

	public CProjectOpener(String path, String name) {
		projectPath = path;
		projectName = name;
	}

	public void importAndOpen() throws URISyntaxException,
			OperationCanceledException, CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		//
		IProject project = root.getProject(projectName);
		IndexerPreferences.set(project, IndexerPreferences.KEY_INDEXER_ID,
				IPDOMManager.ID_NO_INDEXER);

		IProjectDescription description = workspace
				.newProjectDescription(projectName);

		description
				.setLocationURI(new File(FileUtil.getNativePath(projectPath))
						.toURI());

		IPath projectLocation = description.getLocation();

		if (project.exists()) {
			project.delete(true, true, null);
		}

		if ((projectLocation != null)
				&& (!projectLocation.equals(Platform.getLocation()))) {
			description.setLocation(projectLocation);

			CCorePlugin.getDefault().createCProject(description, project, null,
					MakeCorePlugin.MAKE_PROJECT_ID);
			IManagedBuildInfo info = ManagedBuildManager
					.createBuildInfo(project);
			info.setValid(true);
			ManagedCProjectNature.addManagedNature(project, null);
			ManagedCProjectNature.addManagedBuilder(project, null);

			MakeProjectNature.addNature(project, progressMonitor);

			project.open(progressMonitor);

		}

		// @SuppressWarnings("deprecation")
		// public void importAndOpen() throws URISyntaxException,
		// OperationCanceledException, CoreException {
		// IProgressMonitor monitor = new NullProgressMonitor();
		// IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// IWorkspaceRoot root = workspace.getRoot();
		// System.out.println("-1");
		// //
		// IProject project = root.getProject(projectName);
		// IndexerPreferences.set(project, IndexerPreferences.KEY_INDEXER_ID,
		// IPDOMManager.ID_NO_INDEXER);
		//
		// IProjectDescription description = workspace
		// .newProjectDescription(projectName);
		//
		// description
		// .setLocationURI(new File(FileUtil.getNativePath(projectPath))
		// .toURI());
		//
		// project = CCorePlugin.getDefault().createCProject(description,
		// project,
		// new NullProgressMonitor(), MakeCorePlugin.MAKE_PROJECT_ID);
		//
		// if (!project.hasNature(CProjectNature.C_NATURE_ID)) {
		// CProjectNature.addCNature(project, monitor);
		// }
		//
		// if (!project.hasNature(MakeProjectNature.NATURE_ID)) {
		// MakeProjectNature.addNature(project, new SubProgressMonitor(
		// monitor, 1));
		// }
		//
		// // ManagedCProjectNature.addManagedNature(project, monitor);
		//
		// ScannerConfigNature.addScannerConfigNature(project);
		//
		// CCorePlugin.getDefault().mapCProjectOwner(project, projectName,
		// true);
		// //
		// // project.open(new NullProgressMonitor());
		// System.out.println("0");
		// try {
		//
		// ManagedCProjectNature.addManagedNature(project,
		// new SubProgressMonitor(monitor, 1));
		// System.out.println("1");
		// CCorePlugin.getDefault().mapCProjectOwner(project,
		// parse(projectName), true);
		// System.out.println("2");
		//
		// IManagedProject newManagedProject = null;
		// IManagedBuildInfo info = null;
		//
		// try {
		// info = ManagedBuildManager.createBuildInfo(project);
		// IProjectType projectType = ManagedBuildManager
		// .getExtensionProjectType(parse(projectName));
		// newManagedProject = ManagedBuildManager.createManagedProject(
		// project, projectType);
		// System.out.println("2");
		// if (newManagedProject != null) {
		// IConfiguration[] selectedConfigs = projectType
		// .getConfigurations();
		// for (int i = 0; i < selectedConfigs.length; i++) {
		// IConfiguration config = selectedConfigs[i];
		// int id = ManagedBuildManager.getRandomNumber();
		// IConfiguration newConfig = newManagedProject
		// .createConfiguration(config, config.getId()
		// + "." + id);
		// newConfig.setArtifactName(project.getName());
		// }
		// System.out.println("3");
		// IConfiguration[] newConfigs = newManagedProject
		// .getConfigurations();
		// IConfiguration defaultCfg = null;
		// for (int i = 0; i < newConfigs.length; i++) {
		// if (newConfigs[i].isSupported()) {
		// defaultCfg = newConfigs[i];
		// break;
		// }
		// }
		// System.out.println("4");
		//
		// if (defaultCfg == null && newConfigs.length > 0)
		// defaultCfg = newConfigs[0];
		//
		// if (defaultCfg != null) {
		// ManagedBuildManager.setDefaultConfiguration(project,
		// defaultCfg);
		// ManagedBuildManager.setSelectedConfiguration(project,
		// defaultCfg);
		// }
		// ManagedBuildManager.setNewProjectVersion(project);
		// }
		// }
		//
		// catch (BuildException e) {
		// e.printStackTrace();
		//
		// }
		// System.out.println("5");
		// if (info != null) {
		// info.setValid(true);
		// ManagedBuildManager.saveBuildInfo(project, true);
		// }
		//
		// } finally {
		// project.open(monitor);
		// }
		// }
		//
		// public String parse(String string) {
		// return string.replaceAll("-", "//");
	}
}
