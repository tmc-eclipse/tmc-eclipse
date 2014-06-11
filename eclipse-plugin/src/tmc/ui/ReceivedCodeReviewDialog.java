package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

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
        shlCodeReview.setLayout(new FormLayout());

        Label lblCodeReview = new Label(shlCodeReview, SWT.NONE);
        FormData fd_lblCodeReview = new FormData();
        fd_lblCodeReview.bottom = new FormAttachment(0, 33);
        fd_lblCodeReview.right = new FormAttachment(0, 434);
        fd_lblCodeReview.top = new FormAttachment(0, 10);
        fd_lblCodeReview.left = new FormAttachment(0, 10);
        lblCodeReview.setLayoutData(fd_lblCodeReview);
        lblCodeReview.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.BOLD));
        lblCodeReview.setText("Code review - viikko1-002.HeiMaailma");

        Label lblNewLabel = new Label(shlCodeReview, SWT.NONE);
        FormData fd_lblNewLabel = new FormData();
        fd_lblNewLabel.bottom = new FormAttachment(0, 70);
        fd_lblNewLabel.right = new FormAttachment(0, 315);
        fd_lblNewLabel.top = new FormAttachment(0, 47);
        fd_lblNewLabel.left = new FormAttachment(0, 10);
        lblNewLabel.setLayoutData(fd_lblNewLabel);
        lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        lblNewLabel.setText("Reviewed by tmc-eclipse");

        Button btnMarkAsRead = new Button(shlCodeReview, SWT.CHECK);
        FormData fd_btnMarkAsRead = new FormData();
        fd_btnMarkAsRead.bottom = new FormAttachment(0, 69);
        fd_btnMarkAsRead.top = new FormAttachment(0, 45);
        fd_btnMarkAsRead.left = new FormAttachment(0, 321);
        btnMarkAsRead.setLayoutData(fd_btnMarkAsRead);
        btnMarkAsRead.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnMarkAsRead.setText("Mark as read");

        txtHyio = new Text(shlCodeReview, SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
        FormData fd_txtHyio = new FormData();
        fd_txtHyio.bottom = new FormAttachment(0, 356);
        fd_txtHyio.right = new FormAttachment(0, 434);
        fd_txtHyio.top = new FormAttachment(0, 76);
        fd_txtHyio.left = new FormAttachment(0, 10);
        txtHyio.setLayoutData(fd_txtHyio);
        txtHyio.setText("HYI >:O");
        txtHyio.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));

        Button btnNewButton = new Button(shlCodeReview, SWT.NONE);
        FormData fd_btnNewButton = new FormData();
        fd_btnNewButton.bottom = new FormAttachment(0, 417);
        fd_btnNewButton.top = new FormAttachment(0, 385);
        fd_btnNewButton.left = new FormAttachment(0, 10);
        btnNewButton.setLayoutData(fd_btnNewButton);
        btnNewButton.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnNewButton.setText("Open in browser");

        Button btnNewButton_1 = new Button(shlCodeReview, SWT.NONE);
        FormData fd_btnNewButton_1 = new FormData();
        fd_btnNewButton_1.bottom = new FormAttachment(0, 417);
        fd_btnNewButton_1.right = new FormAttachment(0, 434);
        fd_btnNewButton_1.top = new FormAttachment(0, 385);
        fd_btnNewButton_1.left = new FormAttachment(0, 360);
        btnNewButton_1.setLayoutData(fd_btnNewButton_1);
        btnNewButton_1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnNewButton_1.setText("Ok");

    }
}
