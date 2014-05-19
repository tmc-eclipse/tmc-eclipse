package tmc.handlers;

import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.plugin.tmc.Core;

public class CoreInitializer {

	private static Core core;

	public CoreInitializer() {

	}

	public static Core getCore() {
		if (core == null) {
			core = new Core(new EclipseErrorHandler(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell()));

			return core;
		} else {
			return core;
		}
	}
}