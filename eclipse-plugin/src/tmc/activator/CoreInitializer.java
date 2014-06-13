package tmc.activator;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import tmc.handlers.EclipseErrorHandler;
import tmc.spyware.EditorListener;
import tmc.spyware.ResourceEventListener;
import tmc.tasks.EclipseTaskRunner;
import tmc.ui.ExerciseSelectorDialog;
import tmc.ui.LoginDialog;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class CoreInitializer extends AbstractUIPlugin implements IStartup {

    public static final String PLUGIN_ID = "TestMyCode Eclipse plugin"; //$NON-NLS-1$
    private static CoreInitializer instance;

    private WorkbenchHelper workbenchHelper;

    public CoreInitializer() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceEventListener(),
                IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);

        // try {
        // Core.getUpdater().updateCourses();
        // } catch (UserVisibleException uve) {
        // LoginDialog ld = new LoginDialog(new Shell(), SWT.SHEET);
        // ld.open();
        // }
        //
        // Course course = getCurrentCourse();
        //
        // if (course != null) {
        // Core.getUpdater().updateExercises(course);
        // if (!course.getDownloadableExercises().isEmpty()) {
        // ExerciseSelectorDialog esd = new ExerciseSelectorDialog(new Shell(),
        // SWT.SHEET);
        // esd.open();
        // }
        // }

        instance = this;
        this.workbenchHelper = new WorkbenchHelper(Core.getProjectDAO());
    }

    private Course getCurrentCourse() {
        int index = 0;
        for (Course course : Core.getCourseDAO().getCourses()) {
            if (Core.getSettings().getCurrentCourseName().equals(course.getName())) {
                return course;
            }
            index++;
        }
        return null;
    }

    public void stop(BundleContext context) throws Exception {
        Core.getProjectDAO().save();

        instance = null;
        super.stop(context);
    }

    public static CoreInitializer getDefault() {
        return instance;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    @Override
    public void earlyStartup() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                Core.setErrorHandler(new EclipseErrorHandler(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getShell()));
                Core.setTaskRunner(new EclipseTaskRunner());

                if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {

                } else {

                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .addPartListener(new EditorListener());
                }
                try {
                    Core.getUpdater().updateCourses();
                } catch (UserVisibleException uve) {
                    LoginDialog ld = new LoginDialog(new Shell(), SWT.SHEET);
                    ld.open();
                }

                Course course = getCurrentCourse();

                if (course != null) {
                    Core.getUpdater().updateExercises(course);
                    if (!course.getDownloadableExercises().isEmpty()) {
                        ExerciseSelectorDialog esd = new ExerciseSelectorDialog(new Shell(), SWT.SHEET);
                        esd.open();
                    }
                }
            }
        });
    }

    public WorkbenchHelper getWorkbenchHelper() {
        return workbenchHelper;
    }

}