package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class SendToPastebinDialog extends Dialog {

    protected Object result;
    protected Shell shlSendingProjectTo;
    private Text text;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public SendToPastebinDialog(Shell parent, int style) {
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
        shlSendingProjectTo.open();
        shlSendingProjectTo.layout();
        Display display = getParent().getDisplay();
        while (!shlSendingProjectTo.isDisposed()) {
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
        shlSendingProjectTo = new Shell(getParent(), getStyle());
        shlSendingProjectTo.setSize(515, 285);
        shlSendingProjectTo.setText("Sending project to pastebin");

        Label lblNewLabel = new Label(shlSendingProjectTo, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.BOLD));
        lblNewLabel.setBounds(10, 10, 489, 24);
        lblNewLabel.setText("Creating Pastebin item for viikko1-viikko1_002.heimaailma");

        Label lblNewLabel_1 = new Label(shlSendingProjectTo, SWT.NONE);
        lblNewLabel_1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        lblNewLabel_1.setBounds(10, 40, 489, 18);
        lblNewLabel_1.setText("Comment for paste (optional)");

        text = new Text(shlSendingProjectTo, SWT.BORDER);
        text.setBounds(10, 64, 489, 151);

        Button btnCancel = new Button(shlSendingProjectTo, SWT.NONE);
        btnCancel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnCancel.setBounds(431, 221, 68, 29);
        btnCancel.setText("Cancel");

        Button btnSend = new Button(shlSendingProjectTo, SWT.NONE);
        btnSend.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnSend.setBounds(348, 221, 68, 29);
        btnSend.setText("Send");

    }

}
