package tmc.handlers;

import org.eclipse.ui.PlatformUI;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.ErrorHandler;

public class CoreInitializer {

	public CoreInitializer() {
		Core.setErrorHandler(new EclipseErrorHandler(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()));
	}
	
	public static Core getCore(){
		return Core.getInstance();
	}
}