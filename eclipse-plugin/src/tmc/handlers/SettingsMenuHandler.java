package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.ui.ExerciseSelectorDialog;
import tmc.ui.SettingsDialog;
import fi.helsinki.cs.plugin.tmc.Core;


public class SettingsMenuHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		SettingsDialog dialog = new SettingsDialog(window.getShell(), SWT.SHEET);
		dialog.open();
		
		return null;
	}
}
