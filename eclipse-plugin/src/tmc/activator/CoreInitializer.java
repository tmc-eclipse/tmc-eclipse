package tmc.activator;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import tmc.handlers.EclipseErrorHandler;
import tmc.spyware.TestListener;
import tmc.spyware.TestResourceListener;
import tmc.spyware.TestSaveParticipant;
import tmc.tasks.EclipseTaskRunner;
import fi.helsinki.cs.plugin.tmc.Core;

public class CoreInitializer extends AbstractUIPlugin implements IStartup {
    public static final String PLUGIN_ID = "TestMyCode Eclipse plugin"; //$NON-NLS-1$
    private static CoreInitializer instance;

    public CoreInitializer() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
		ResourcesPlugin.getWorkspace().addSaveParticipant("tmc-eclipse", new TestSaveParticipant());
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new TestResourceListener(), 
				IResourceChangeEvent.POST_CHANGE);
        instance = this;
    }

    public void stop(BundleContext context) throws Exception {
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
                Display.getCurrent().addFilter(SWT.Modify, new TestListener());
            }
        });

    }
}