package tmc.handlers;

import java.io.File;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;

public class TestRunnerHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {

        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {

        }

        IProgressMonitor monitor = new NullProgressMonitor();
        AntRunner runner = new AntRunner();
        runner.setBuildFileLocation("/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs/build.xml");
        runner.setArguments("-Dmessage=Building -verbose");

        String[] target = {"compile-test"};
        runner.setExecutionTargets(target);
        try {
            runner.run(monitor);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String classpath = "/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs/bin/;"
                + "/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs/lib/junit-4.10.jar;"
                + "/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs/lib/edu-test-utils-0.4.1.jar;"
                + "/cs/fs2/home/jphelio/Desktop/eclipse/configuration/org.eclipse.osgi/bundles/165/1/.cp/;"
                + "/cs/fs2/home/jphelio/Desktop/eclipse/configuration/org.eclipse.osgi/bundles/164/1/.cp/;"
                + "/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs/test";

        // rootDir, testDir, resultFile, testClasspath, memorylimit
        new TestrunnerTask(new File("/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs"), new File(
                "/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs/test"), new File(
                "/cs/fs2/home/jphelio/Desktop/eclipseAntTest/arith_funcs/results.txt"), null, null).start(null);

        return null;
    }
}
