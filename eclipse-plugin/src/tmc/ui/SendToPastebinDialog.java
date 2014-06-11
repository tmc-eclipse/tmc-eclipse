package tmc.ui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import tmc.tasks.TaskStarter;

public class SendToPastebinDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private Text text;
    private String exerciseName;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param exerciseName
     */
    public SendToPastebinDialog(Shell parent, String exerciseName) {
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
        shell.setSize(515, 285);
        shell.setText("Sending project to pastebin");

        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.BOLD));
        lblNewLabel.setBounds(10, 10, 489, 24);
        lblNewLabel.setText("Creating Pastebin item for "+exerciseName);

        Label lblNewLabel_1 = new Label(shell, SWT.NONE);
        lblNewLabel_1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        lblNewLabel_1.setBounds(10, 40, 489, 18);
        lblNewLabel_1.setText("Comment for paste (optional)");

        text = new Text(shell, SWT.BORDER | SWT.MULTI);
        text.setBounds(10, 64, 489, 151);

        Button btnCancel = new Button(shell, SWT.NONE);
        btnCancel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnCancel.setBounds(431, 221, 68, 29);
        btnCancel.setText("Cancel");
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });

        Button btnSend = new Button(shell, SWT.NONE);
        btnSend.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnSend.setBounds(348, 221, 68, 29);
        btnSend.setText("Send");
        btnSend.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TaskStarter.startPastebinTask(new EclipseIdeUIInvoker(shell.getParent().getShell()), text.getText());
                shell.close();
            }
        });
    }
}
