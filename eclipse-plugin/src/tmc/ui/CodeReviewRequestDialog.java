package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class CodeReviewRequestDialog extends Dialog {

    protected Object result;
    protected Shell shlConfirmCodeReview;
    private Text txtKattokaaMunParasta;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public CodeReviewRequestDialog(Shell parent, int style) {
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
        shlConfirmCodeReview.open();
        shlConfirmCodeReview.layout();
        Display display = getParent().getDisplay();
        while (!shlConfirmCodeReview.isDisposed()) {
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
        shlConfirmCodeReview = new Shell(getParent(), getStyle());
        shlConfirmCodeReview.setSize(420, 285);
        shlConfirmCodeReview.setText("Confirm code review request");

        Label lblRequestingCodeReview = new Label(shlConfirmCodeReview, SWT.NONE);
        lblRequestingCodeReview.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 11, SWT.BOLD));
        lblRequestingCodeReview.setBounds(10, 10, 394, 24);
        lblRequestingCodeReview.setText("Requesting code review for viikko1-002.heimaailma");

        Label lblMessageForThe = new Label(shlConfirmCodeReview, SWT.NONE);
        lblMessageForThe.setFont(SWTResourceManager.getFont("MS Sans Serif", 11, SWT.NORMAL));
        lblMessageForThe.setBounds(10, 40, 252, 16);
        lblMessageForThe.setText("Message for the reviewer (optional):");

        txtKattokaaMunParasta = new Text(shlConfirmCodeReview, SWT.BORDER);
        txtKattokaaMunParasta.setText("KATTOKAA MUN PARASTA KOODIA PLZ");
        txtKattokaaMunParasta.setFont(SWTResourceManager.getFont("Tahoma", 11, SWT.NORMAL));
        txtKattokaaMunParasta.setBounds(10, 58, 384, 150);

        Button btnNewButton = new Button(shlConfirmCodeReview, SWT.NONE);
        btnNewButton.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnNewButton.setBounds(329, 215, 75, 35);
        btnNewButton.setText("Cancel");

        Button btnNewButton_1 = new Button(shlConfirmCodeReview, SWT.NONE);
        btnNewButton_1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnNewButton_1.setBounds(202, 215, 121, 35);
        btnNewButton_1.setText("Request review");

    }

}
