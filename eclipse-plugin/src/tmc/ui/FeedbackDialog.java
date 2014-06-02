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
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public class FeedbackDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private Text text;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public FeedbackDialog(Shell parent, int style) {
        super(parent, style);
        setText("Server results");
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
        shell.setSize(370, 392);
        shell.setText(getText());

        Button btnOk = new Button(shell, SWT.NONE);
        btnOk.setText("OK");
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        btnOk.setBounds(290, 323, 64, 29);

        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Ubuntu", 12, SWT.NORMAL));
        lblNewLabel.setText("All tests passed on the server.");
        lblNewLabel.setForeground(SWTResourceManager.getColor(34, 139, 34));
        lblNewLabel.setBounds(53, 10, 301, 37);

        Label lblNewLabel_1 = new Label(shell, SWT.NONE);
        lblNewLabel_1.setImage(ResourceManager.getPluginImage("eclipse-plugin", "resources/smile.gif"));
        lblNewLabel_1.setBounds(10, 0, 37, 47);

        Label lblPointsPermanentlyAwarded = new Label(shell, SWT.NONE);
        lblPointsPermanentlyAwarded.setBounds(10, 53, 344, 17);
        lblPointsPermanentlyAwarded.setText("Points permanently awarded: 1.");

        Button btnViewModelSolution = new Button(shell, SWT.NONE);
        btnViewModelSolution.setBounds(10, 76, 163, 29);
        btnViewModelSolution.setText("View model solution");

        Label lblFeedbackleaveEmpty = new Label(shell, SWT.NONE);
        lblFeedbackleaveEmpty.setBounds(10, 118, 250, 17);
        lblFeedbackleaveEmpty.setText("Feedback (leave empty to not send):");

        Label lblMitenVaikealtaTehtv = new Label(shell, SWT.NONE);
        lblMitenVaikealtaTehtv.setFont(SWTResourceManager.getFont("Ubuntu", 10, SWT.NORMAL));
        lblMitenVaikealtaTehtv.setText("Miten vaikealta tehtävä tuntui?");
        lblMitenVaikealtaTehtv.setBounds(26, 150, 325, 17);

        Button btnRadioButton = new Button(shell, SWT.RADIO);
        btnRadioButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        btnRadioButton.setBounds(136, 173, 37, 24);
        btnRadioButton.setText("1");

        Button button = new Button(shell, SWT.RADIO);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        button.setText("2");
        button.setBounds(179, 173, 37, 24);

        Button button_1 = new Button(shell, SWT.RADIO);
        button_1.setText("3");
        button_1.setBounds(222, 173, 37, 24);

        Button button_2 = new Button(shell, SWT.RADIO);
        button_2.setText("4");
        button_2.setBounds(265, 173, 37, 24);

        Button button_3 = new Button(shell, SWT.RADIO);
        button_3.setText("5");
        button_3.setBounds(308, 173, 37, 24);

        Button btnNa = new Button(shell, SWT.RADIO);
        btnNa.setSelection(true);
        btnNa.setBounds(26, 173, 52, 24);
        btnNa.setText("N/A");

        Button button_4 = new Button(shell, SWT.RADIO);
        button_4.setBounds(93, 173, 37, 24);
        button_4.setText("0");

        Label lblKommentteja = new Label(shell, SWT.NONE);
        lblKommentteja.setText("Kommentteja?");
        lblKommentteja.setFont(SWTResourceManager.getFont("Ubuntu", 10, SWT.NORMAL));
        lblKommentteja.setBounds(26, 210, 325, 17);

        text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        text.setBounds(29, 233, 325, 72);
    }
}
