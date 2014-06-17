package tmc.activator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import tmc.tasks.CheckForCodeReviewsOnBackgroundTask;
import tmc.tasks.EclipseTaskRunner;
import tmc.ui.ExerciseSelectorDialog;
import tmc.ui.LoginDialog;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class CoreInitializer extends AbstractUIPlugin implements IStartup {

    public static final String PLUGIN_ID = "TestMyCode Eclipse plugin"; //$NON-NLS-1$
    private static CoreInitializer instance;

    private WorkbenchHelper workbenchHelper;

    public CoreInitializer() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);

        ServerManager server = Core.getServerManager();

        ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceEventListener(),
                IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);

        instance = this;

        this.workbenchHelper = new WorkbenchHelper(Core.getProjectDAO());

        startRecurringTasks();
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

                if (!(PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null)) {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .addPartListener(new EditorListener());
                }
                try {
                    Core.getUpdater().updateCourses();
                } catch (UserVisibleException uve) {
                    LoginDialog ld = new LoginDialog(new Shell(), SWT.SHEET);
                    ld.open();
                }

                // First checks if the shell of the display is null and then if
                // it is creates a new exercisedialog with new shell
                // This can happen if the user after ide has opened clicks away
                // from ide thus changing the focus out of ide.
                if (Display.getDefault().getActiveShell() != null) {
                    ExerciseSelectorDialog esd = new ExerciseSelectorDialog(Display.getDefault().getActiveShell(),
                            SWT.SHEET);
                    esd.open();
                } else {
                    ExerciseSelectorDialog esd = new ExerciseSelectorDialog(new Shell(), SWT.SHEET);
                    esd.open();
                }
            }
        });
    }

    public WorkbenchHelper getWorkbenchHelper() {
        return workbenchHelper;
    }

    private void startRecurringTasks() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new CheckForCodeReviewsOnBackgroundTask(), 5, 5, TimeUnit.SECONDS);
    }
}