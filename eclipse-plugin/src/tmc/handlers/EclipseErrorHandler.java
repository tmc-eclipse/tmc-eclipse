package tmc.handlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fi.helsinki.cs.plugin.tmc.TMCErrorHandler;

public class EclipseErrorHandler implements TMCErrorHandler {

    private Shell parent;

    public EclipseErrorHandler(Shell parent) {
        this.parent = parent;
    }

    @Override
    public void handleException(final Exception e) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog dialog = new MessageDialog(parent, "Error", null, e.getMessage(), MessageDialog.ERROR,
                        new String[] {"OK"}, 0);
                dialog.open();
            }
        });
    }

}
