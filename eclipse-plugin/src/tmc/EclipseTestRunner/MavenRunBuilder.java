package tmc.EclipseTestRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.jdt.MavenJdtPlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

public class MavenRunBuilder {

    private File projectDir = null;
    private List<String> goals = new ArrayList<String>();
    private Map<String, String> props = new HashMap<String, String>();
    private IViewPart trv = null;
    private String classPath = System.getProperty("java.class.path");

    public MavenRunBuilder() {
    }

    public MavenRunBuilder setProjectDir(File projectDir) {
        this.projectDir = projectDir;
        return this;
    }

    public MavenRunBuilder addGoal(String goal) {
        goals.add(goal);
        return this;
    }

    public MavenRunBuilder setProperty(String key, String value) {
        props.put(key, value);
        return this;
    }

    public MavenRunBuilder setProperties(Map<String, String> props) {
        this.props.putAll(props);
        return this;
    }

    public MavenRunBuilder setIO(IViewPart trv) {
        this.trv = trv;
        return this;
    }

    public ProcessRunner createProcessRunner() {
        if (projectDir == null) {
            throw new IllegalStateException("Project dir not set");
        }
        if (trv == null) {
            throw new IllegalStateException("Maven IO not set");
        }

        // JavaPlatform platform = JavaPlatform.getDefault(); // Should probably
        // use project's
        // configured
        // platform instead
        File javaExe = new File("java");
        if (javaExe == null) {
            throw new RuntimeException("Java executable not found");
        }

        final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();

        IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput();
        IFile file = input.getFile();
        IProject activeProject = file.getProject();

        String classPath = "";
        try {
            classPath = MavenJdtPlugin.getDefault().getBuildpathManager().getClasspath(activeProject, 0, true, null)
                    .toString();
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String[] command = buildCommand(javaExe, classPath);

        return new ProcessRunner(command, projectDir);
    }

    private String[] buildCommand(File javaExe, String cp) {
        List<String> command = new ArrayList<String>(32);

        command.add(javaExe.getPath());
        command.add("-cp");
        command.add(cp);
        command.add("org.apache.maven.cli.MavenCli");
        for (String key : props.keySet()) {
            command.add("-D" + key + "=" + props.get(key));
        }
        command.addAll(goals);

        return command.toArray(new String[command.size()]);
    }
}
