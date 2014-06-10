package tmc.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

import tmc.activator.CoreInitializer;

public class PastebinResultDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private Text text;
    private String pasteUrl;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public PastebinResultDialog(Shell parent, String pasteUrl) {
        super(parent, SWT.SHEET);
        setText("SWT Dialog");
        this.pasteUrl = pasteUrl;
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
        shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        shell.setSize(450, 210);
        shell.setText("Pastebin notification\r\n");

        Label lblCodeSubmittedTo = new Label(shell, SWT.NONE);
        lblCodeSubmittedTo.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        lblCodeSubmittedTo.setBounds(10, 10, 265, 19);
        lblCodeSubmittedTo.setText("Code submitted to TMC pastebin.");

        Button btnViewPaste = new Button(shell, SWT.NONE);
        btnViewPaste.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnViewPaste.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CoreInitializer.getDefault().getWorkbenchHelper().openURL(pasteUrl);
            }
        });
        btnViewPaste.setBounds(10, 35, 99, 33);
        btnViewPaste.setText("View paste");

        text = new Text(shell, SWT.BORDER);
        text.setBounds(10, 74, 424, 25);
        text.setText(pasteUrl);

        Button btnOK = new Button(shell, SWT.NONE);
        btnOK.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnOK.setBounds(384, 105, 50, 33);
        btnOK.setText("Ok");
        btnOK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });

        Button btnCopyToClipboard = new Button(shell, SWT.NONE);
        btnCopyToClipboard.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 12, SWT.NORMAL));
        btnCopyToClipboard.setBounds(282, 144, 152, 31);
        btnCopyToClipboard.setText("Copy to clipboard");
        btnCopyToClipboard.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Clipboard clipboard = new Clipboard(Display.getCurrent());
                TextTransfer textTransfer = TextTransfer.getInstance();
                clipboard.setContents(new Object[] {text.getText()}, new Transfer[] {textTransfer});
                clipboard.dispose();
            }
        });
    }
}
