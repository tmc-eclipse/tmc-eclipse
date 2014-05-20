package tmc.handlers;

import org.eclipse.ui.PlatformUI;
import fi.helsinki.cs.plugin.tmc.Core;

public class CoreInitializer {

	public CoreInitializer() {
		Core.setMyLittleErrorHandler(new EclipseErrorHandler(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()));
	}
	
	public static Core getCore(){
		return Core.getInstance();
	}
}