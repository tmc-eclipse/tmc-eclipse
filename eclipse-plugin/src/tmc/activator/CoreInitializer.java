package tmc.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import fi.helsinki.cs.plugin.tmc.Core;

public class CoreInitializer extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "TestMyCode Eclipse plugin"; //$NON-NLS-1$
	private static CoreInitializer instance;
	
	public CoreInitializer() {
		//Core.setMyLittleErrorHandler(new EclipseErrorHandler(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()));
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
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
	
}