package tmc.eclipse.activator;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import tmc.eclipse.handlers.EclipseErrorHandler;
import tmc.eclipse.spyware.EditorListener;
import tmc.eclipse.spyware.ResourceEventListener;
import tmc.eclipse.tasks.EclipseTaskRunner;
import tmc.eclipse.tasks.RecurringTaskRunner;
import tmc.eclipse.util.WorkbenchHelper;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;

public class CoreInitializer extends AbstractUIPlugin implements IStartup {

    public static final String PLUGIN_ID = "TestMyCode Eclipse plugin"; //$NON-NLS-1$
    private static CoreInitializer instance;

    private WorkbenchHelper workbenchHelper;
    private RecurringTaskRunner recurringTaskRunner;

    public CoreInitializer() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);

        ServerManager server = Core.getServerManager();

        ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceEventListener(),
                IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);

        instance = this;

        this.workbenchHelper = new WorkbenchHelper(Core.getProjectDAO());

        Core.setTaskRunner(new EclipseTaskRunner());
        recurringTaskRunner = new RecurringTaskRunner(Core.getSettings());
        recurringTaskRunner.startRecurringTasks();
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
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                Core.setErrorHandler(new EclipseErrorHandler(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getShell()));

                if (!(PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null)) {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .addPartListener(new EditorListener());
                }
            }
        });
    }

    public WorkbenchHelper getWorkbenchHelper() {
        return workbenchHelper;
    }

    public RecurringTaskRunner getRecurringTaskRunner() {
        return recurringTaskRunner;
    }
}