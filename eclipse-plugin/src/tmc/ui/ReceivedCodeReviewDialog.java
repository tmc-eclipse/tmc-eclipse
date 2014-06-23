package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import tmc.activator.CoreInitializer;
import tmc.tasks.TaskStarter;
import fi.helsinki.cs.plugin.tmc.domain.Review;

public class ReceivedCodeReviewDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private Text reviewBody;
    private Review review;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public ReceivedCodeReviewDialog(Shell parent, Review review) {
        super(parent, SWT.SHEET);
        setText("SWT Dialog");
        this.review = review;
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
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setSize(450, 475);
        shell.setText("Code review");
        shell.setLayout(null);

        Label lblCodeReview = new Label(shell, SWT.NONE);
        lblCodeReview.setBounds(10, 10, 424, 23);
        lblCodeReview.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.BOLD));
        lblCodeReview.setText("Code review - " + review.getExerciseName());

        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setBounds(10, 47, 274, 23);
        lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        lblNewLabel.setText("Reviewed by " + review.getReviewerName());

        final Button btnMarkAsRead = new Button(shell, SWT.CHECK);
        btnMarkAsRead.setBounds(290, 46, 144, 24);
        btnMarkAsRead.setSelection(true);
        btnMarkAsRead.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnMarkAsRead.setText("Mark as read");

        reviewBody = new Text(shell, SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
        reviewBody.setBounds(10, 76, 424, 280);
        reviewBody.setText(review.getReviewBody());
        reviewBody.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));

        Button btnOpenInBrowser = new Button(shell, SWT.NONE);
        btnOpenInBrowser.setBounds(10, 385, 164, 32);
        btnOpenInBrowser.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnOpenInBrowser.setText("Open in browser");
        btnOpenInBrowser.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CoreInitializer.getDefault().getWorkbenchHelper().openURL(review.getUrl());
            }
        });

        Button btnOK = new Button(shell, SWT.NONE);
        btnOK.setBounds(360, 385, 74, 32);
        btnOK.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnOK.setText("Ok");
        btnOK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnMarkAsRead.getSelection()) {
                    TaskStarter.startMarkCodereviewAsReadTask(review);
                }
                shell.close();
            }
        });
    }
}
