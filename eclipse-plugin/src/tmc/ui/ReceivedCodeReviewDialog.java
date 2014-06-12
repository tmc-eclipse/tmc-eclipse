package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ReceivedCodeReviewDialog extends Dialog {

    protected Object result;
    protected Shell shlCodeReview;
    private Text txtHyio;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public ReceivedCodeReviewDialog(Shell parent, int style) {
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
        shlCodeReview.open();
        shlCodeReview.layout();
        Display display = getParent().getDisplay();
        while (!shlCodeReview.isDisposed()) {
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
        shlCodeReview = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shlCodeReview.setSize(450, 475);
        shlCodeReview.setText("Code review");
        shlCodeReview.setLayout(null);

        Label lblCodeReview = new Label(shlCodeReview, SWT.NONE);
        lblCodeReview.setBounds(10, 10, 424, 23);
        lblCodeReview.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.BOLD));
        lblCodeReview.setText("Code review - viikko1-002.HeiMaailma");

        Label lblNewLabel = new Label(shlCodeReview, SWT.NONE);
        lblNewLabel.setBounds(10, 47, 274, 23);
        lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        lblNewLabel.setText("Reviewed by tmc-eclipse");

        Button btnMarkAsRead = new Button(shlCodeReview, SWT.CHECK);
        btnMarkAsRead.setBounds(290, 46, 144, 24);
        btnMarkAsRead.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnMarkAsRead.setText("Mark as read");

        txtHyio = new Text(shlCodeReview, SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
        txtHyio.setBounds(10, 76, 424, 280);
        txtHyio.setText("HYI >:O");
        txtHyio.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));

        Button btnNewButton = new Button(shlCodeReview, SWT.NONE);
        btnNewButton.setBounds(10, 385, 144, 32);
        btnNewButton.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnNewButton.setText("Open in browser");

        Button btnNewButton_1 = new Button(shlCodeReview, SWT.NONE);
        btnNewButton_1.setBounds(360, 385, 74, 32);
        btnNewButton_1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnNewButton_1.setText("Ok");

    }
}
