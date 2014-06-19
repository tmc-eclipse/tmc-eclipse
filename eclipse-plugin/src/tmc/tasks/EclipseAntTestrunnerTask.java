package tmc.tasks;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import fi.helsinki.cs.plugin.tmc.async.tasks.AntTestrunnerTask;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class EclipseAntTestrunnerTask extends AntTestrunnerTask {

    public EclipseAntTestrunnerTask(String rootPath, String testDir, String javaExecutable, Integer memoryLimit,
            Settings settings, IdeUIInvoker invoker) {
        super(rootPath, testDir, javaExecutable, memoryLimit, settings, invoker);
    }

    @Override
    public void build(final String root) throws Exception {
        IProgressMonitor monitor = new NullProgressMonitor();
        AntRunner runner = new AntRunner();
        runner.setBuildFileLocation(root + "/build.xml");
        runner.setArguments(new String[] {"compile-test"});
        runner.run(monitor);
    }
}
