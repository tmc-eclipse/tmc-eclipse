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

import tmc.tasks.TaskStarter;

public class CodeReviewRequestDialog extends Dialog {

    protected Object result;
    protected Shell shell;

    private Text text;
    private String exerciseName;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public CodeReviewRequestDialog(Shell parent, String exerciseName) {
        super(parent, SWT.SHEET);
        setText("SWT Dialog");
        this.exerciseName = exerciseName;
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
        shell.setSize(420, 285);
        shell.setText("Confirm code review request");

        Label lblTitle = new Label(shell, SWT.NONE);
        lblTitle.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 11, SWT.BOLD));
        lblTitle.setBounds(10, 10, 394, 24);
        lblTitle.setText("Requesting code review for " + exerciseName);

        Label lblMessage = new Label(shell, SWT.NONE);
        lblMessage.setFont(SWTResourceManager.getFont("MS Sans Serif", 11, SWT.NORMAL));
        lblMessage.setBounds(10, 40, 269, 16);
        lblMessage.setText("Message for the reviewer (optional):");

        text = new Text(shell, SWT.BORDER | SWT.MULTI);
        text.setFont(SWTResourceManager.getFont("Tahoma", 11, SWT.NORMAL));
        text.setBounds(10, 58, 384, 150);

        Button btnCancel = new Button(shell, SWT.NONE);
        btnCancel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnCancel.setBounds(329, 215, 85, 35);
        btnCancel.setText("Cancel");
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });

        Button btnRequest = new Button(shell, SWT.NONE);
        btnRequest.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnRequest.setBounds(179, 215, 144, 35);
        btnRequest.setText("Request review");
        btnRequest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TaskStarter.startCodeReviewRequestTask(new EclipseIdeUIInvoker(shell.getParent().getShell()),
                        text.getText());
                shell.close();
            }
        });

    }

}
