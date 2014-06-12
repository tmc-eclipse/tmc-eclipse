package tmc.handlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fi.helsinki.cs.plugin.tmc.TMCErrorHandler;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class EclipseErrorHandler implements TMCErrorHandler {

	private Shell parent;

	public EclipseErrorHandler(Shell parent) {
		this.parent = parent;
	}

	@Override
	public void raise(String message) {
		handleException(new UserVisibleException(message));
	}

	@Override
	public void handleException(final Exception e) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog dialog = new MessageDialog(parent, "Error", null,
						e.getMessage(), MessageDialog.ERROR,
						new String[] { "OK" }, 0);
				dialog.open();
			}
		});
	}

	public void handleManualException(final String errorMessage) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog dialog = new MessageDialog(parent, "Error", null,
						errorMessage, MessageDialog.ERROR,
						new String[] { "OK" }, 0);
				dialog.open();
			}
		});
	}

}
