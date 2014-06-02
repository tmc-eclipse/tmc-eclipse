package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SubmitToServerDialog extends Dialog {

    protected Object result;
    protected Shell shell;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public SubmitToServerDialog(Shell parent, int style) {
        super(parent, style);
        setText("SWT Dialog");
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        shell.setSize(256, 86);
        shell.setText(getText());

        Label lblSdgsgs = new Label(shell, SWT.NONE);
        lblSdgsgs.setBounds(10, 10, 330, 17);
        lblSdgsgs.setText("All tests passed. Submit to server?");

        Button btnYes = new Button(shell, SWT.NONE);
        btnYes.setBounds(122, 43, 59, 29);
        btnYes.setText("Yes");

        Button btnNo = new Button(shell, SWT.NONE);
        btnNo.setBounds(184, 43, 59, 29);
        btnNo.setText("No");

    }

}
