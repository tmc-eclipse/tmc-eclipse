package tmc.handlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import fi.helsinki.cs.plugin.tmc.ErrorHandler;

public class EclipseErrorHandler implements ErrorHandler {
	
	private Shell parent;
	
	public EclipseErrorHandler(Shell parent){
		this.parent = parent;
	}
	
	@Override
	public void handleException(Exception e) {
		MessageDialog dialog = new MessageDialog(parent, "ERROR", null, e.getMessage(), MessageDialog.ERROR, new String[] {"OK"}, 0);
		dialog.open();
		
	}

}
