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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
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

		IProject project = root.getProject(projectName);
		IndexerPreferences.set(project, IndexerPreferences.KEY_INDEXER_ID,
				IPDOMManager.ID_NO_INDEXER);

		IProjectDescription description = workspace
				.newProjectDescription(projectName);

		description.setLocationURI(new URI(projectPath));

		project = CCorePlugin.getDefault().createCProject(description, project,
				new NullProgressMonitor(), projectName);

		MakeProjectNature.addNature(project, new NullProgressMonitor());

		MakeProjectNature.addToBuildSpec(project, "CBuilder",
				new NullProgressMonitor());

		project.open(new NullProgressMonitor());

	}
}
