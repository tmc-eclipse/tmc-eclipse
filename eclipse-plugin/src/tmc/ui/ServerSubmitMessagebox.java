package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ServerSubmitMessagebox {

    private int response;

    public ServerSubmitMessagebox(Shell shell) {
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.NO | SWT.YES);
        messageBox.setMessage("All tests passed. Submit to server?");
        messageBox.setText("Submit?");
        this.response = messageBox.open();
    }

    public boolean getResponse() {
        return response == SWT.YES;
    }

}
