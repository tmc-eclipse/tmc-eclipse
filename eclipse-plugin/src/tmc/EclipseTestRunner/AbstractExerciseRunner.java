package tmc.EclipseTestRunner;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;

public abstract class AbstractExerciseRunner implements ExerciseRunner {

    protected static final String ERROR_MSG_LOCALE_SETTING = "fi.helsinki.cs.tmc.edutestutils.defaultLocale";

    protected TestResultParser resultParser;

    public AbstractExerciseRunner() {
        this.resultParser = new TestResultParser();
    }

    protected Integer getMemoryLimit(Project project) {
        Exercise ex = project.getExercise();
        if (ex != null) {
            return ex.getMemoryLimit();
        } else {
            return null;
        }
    }

    protected IViewPart getIoTab() {
        try {
            return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {

        }

        return null;
    }

    // protected Callable<Integer> executorTaskToCallable(final ExecutorTask et)
    // {
    // return new Callable<Integer>() {
    // @Override
    // public Integer call() throws Exception {
    // return et.result();
    // }
    // };
    // }
}
