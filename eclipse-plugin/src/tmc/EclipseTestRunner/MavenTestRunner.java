package tmc.EclipseTestRunner;

import java.io.File;
import java.util.concurrent.Callable;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import tmc.ui.TestRunnerView;

public class MavenTestRunner {
    
    private IViewPart trv;
    
    public Callable<Integer> getCompilingTask(String projectInfo) {
        File projectDir = new File(projectInfo);

        String goal = "test-compile";
        try {
            trv = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {}

        final ProcessRunner runner = new MavenRunBuilder()
                .setProjectDir(projectDir)
                .addGoal(goal)
                .setIO(trv)
                .createProcessRunner();

        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    ProcessResult result = runner.call();
                    int ret = result.statusCode;
                    if (ret != 0) {
                        trv.setFocus();
                    }
                    return ret;
                } catch (Exception ex) {
                    trv.setFocus();
                    throw ex;
                }
            }
        };
    }


}
