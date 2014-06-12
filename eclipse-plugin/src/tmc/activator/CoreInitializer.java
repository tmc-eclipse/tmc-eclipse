package tmc.activator;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import tmc.handlers.EclipseErrorHandler;
import tmc.spyware.EditorListener;
import tmc.spyware.ResourceEventListener;
import tmc.tasks.EclipseTaskRunner;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.Core;

public class CoreInitializer extends AbstractUIPlugin implements IStartup {
    public static final String PLUGIN_ID = "TestMyCode Eclipse plugin"; //$NON-NLS-1$
    private static CoreInitializer instance;

    private WorkbenchHelper workbenchHelper;

    public CoreInitializer() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        // ResourcesPlugin.getWorkspace().addSaveParticipant("tmc-eclipse", new
        // TestSaveParticipant());
        ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceEventListener(),
                IResourceChangeEvent.POST_CHANGE);

        instance = this;
        this.workbenchHelper = new WorkbenchHelper(Core.getProjectDAO());
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
                // Display.getCurrent().addFilter(SWT.Modify, new
                // TestListener());
                // Display.getCurrent().addFilter(SWT.Verify, new
                // TestListener());
                // Display.getCurrent().addFilter(SWT., new TestListener());

                if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
                    System.out.println("Null wb");

                } else {

                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .addPartListener(new EditorListener());
                }
            }
        });
    }

    public WorkbenchHelper getWorkbenchHelper() {
        return workbenchHelper;
    }

}