package tmc.eclipse.tasks;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import fi.helsinki.cs.tmc.core.async.tasks.AntTestrunnerTask;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class EclipseAntTestrunnerTask extends AntTestrunnerTask {

    public EclipseAntTestrunnerTask(String rootPath, String testDir, String javaExecutable, Integer memoryLimit,
            Settings settings, IdeUIInvoker invoker) {
        super(rootPath, testDir, javaExecutable, memoryLimit, settings, invoker);
    }

    @Override
    public void build(final String root) throws ConcurrentAntBuildsException, Exception {
        try {
            IProgressMonitor monitor = new NullProgressMonitor();
            AntRunner runner = new AntRunner();
            runner.setBuildFileLocation(root + "/build.xml");
            runner.setArguments(new String[] {"compile-test"});
            runner.run(monitor);
        } catch (CoreException e) {
            String concurrentAntBuildsMessage = "Concurrent Ant builds are possible if you specify to build in a separate JRE.";
            if (e.getMessage().endsWith(concurrentAntBuildsMessage)) {
                throw new ConcurrentAntBuildsException();
            } else {
                throw e;
            }
        }
    }
}
