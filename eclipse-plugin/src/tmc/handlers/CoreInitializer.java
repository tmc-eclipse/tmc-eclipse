package tmc.handlers;

import org.eclipse.ui.PlatformUI;
import fi.helsinki.cs.plugin.tmc.Core;

public class CoreInitializer {

	public CoreInitializer() {
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		Core.setMyLittleErrorHandler(new EclipseErrorHandler(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()));
	}
	
	public static Core getCore(){
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
		return Core.getInstance();
	}
}