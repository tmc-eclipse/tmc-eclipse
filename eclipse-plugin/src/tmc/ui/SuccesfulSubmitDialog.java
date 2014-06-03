package tmc.ui;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackQuestion;

public class SuccesfulSubmitDialog extends Dialog {

    public static final int QUESTIONS_HEIGHT_OFFSET = 150;

    public static final int RATING_QUESTION_HEIGHT = 55;
    public static final int TEXT_QUESTION_HEIGHT = 105;

    protected Object result;
    protected Shell shell;
    private Text text;

    private int pointsAwarded;
    private String modelSolutionUrl;

    private List<FeedbackQuestion> questions;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public SuccesfulSubmitDialog(Shell parent, String title) {
        super(parent, SWT.SHEET);

        questions = new ArrayList<FeedbackQuestion>();

        pointsAwarded = 0;
        modelSolutionUrl = "";

        setText(title);
    }

    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public void setModelSolutionUrl(String modelSolutionUrl) {
        this.modelSolutionUrl = modelSolutionUrl;
    }

    public void addFeedbackQuestion(FeedbackQuestion question) {
        this.questions.add(question);
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
        shell.setSize(320, 170);
        shell.setText(getText());

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
        lblPointsPermanentlyAwarded.setText("Points permanently awarded: " + pointsAwarded + ".");

        Button btnViewModelSolution = new Button(shell, SWT.NONE);
        btnViewModelSolution.setBounds(10, 76, 163, 29);
        btnViewModelSolution.setText("View model solution");
        btnViewModelSolution.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!modelSolutionUrl.isEmpty()) {
                    openUrl(modelSolutionUrl);
                } else {
                    Core.getErrorHandler().raise("There is no model solution available for this exercise");
                }
            }
        });

        int heightOffset = createFeedbackForm();
        shell.setSize(370, 75 + heightOffset);

        Button btnOk = new Button(shell, SWT.NONE);
        btnOk.setText("OK");
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        btnOk.setBounds(290, 10 + heightOffset, 64, 29);
    }

    private void openUrl(String modelSolutionUrl) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(modelSolutionUrl));
            } catch (Exception e) {
            }
        }
    }

    private int createFeedbackForm() {
        int heightOffset = QUESTIONS_HEIGHT_OFFSET;

        if (questions.isEmpty()) {
            return heightOffset - 20;
        }

        Label lblFeedbackleaveEmpty = new Label(shell, SWT.NONE);
        lblFeedbackleaveEmpty.setBounds(10, 118, 250, 17);
        lblFeedbackleaveEmpty.setText("Feedback (leave empty to not send):");

        for (FeedbackQuestion question : questions) {
            if (question.isIntRange()) {
                addRatingFeedbackField(question.getQuestion(), heightOffset);
                heightOffset += RATING_QUESTION_HEIGHT;
            }
            if (question.isText()) {
                addTextFeedbackField(question.getQuestion(), heightOffset);
                heightOffset += TEXT_QUESTION_HEIGHT;
            }
        }

        return heightOffset;
    }

    private void addTextFeedbackField(String label, int heightOffset) {
        Label lblKommentteja = new Label(shell, SWT.NONE);
        lblKommentteja.setText(label);
        lblKommentteja.setFont(SWTResourceManager.getFont("Ubuntu", 10, SWT.NORMAL));
        lblKommentteja.setBounds(26, heightOffset, 325, 17);

        text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        text.setBounds(29, heightOffset + 23, 325, 72);
    }

    private void addRatingFeedbackField(String label, int heightOffset) {
        Label lblMitenVaikealtaTehtv = new Label(shell, SWT.NONE);
        lblMitenVaikealtaTehtv.setFont(SWTResourceManager.getFont("Ubuntu", 10, SWT.NORMAL));
        lblMitenVaikealtaTehtv.setText(label);
        lblMitenVaikealtaTehtv.setBounds(26, heightOffset, 325, 17);

        Button btnRadioButton = new Button(shell, SWT.RADIO);
        btnRadioButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });

        Button btnNa = new Button(shell, SWT.RADIO);
        btnNa.setSelection(true);
        btnNa.setBounds(26, heightOffset + 23, 52, 24);
        btnNa.setText("N/A");

        btnRadioButton.setBounds(136, heightOffset + 23, 37, 24);
        btnRadioButton.setText("1");

        Button button = new Button(shell, SWT.RADIO);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        button.setText("2");
        button.setBounds(179, heightOffset + 23, 37, 24);

        Button button_1 = new Button(shell, SWT.RADIO);
        button_1.setText("3");
        button_1.setBounds(222, heightOffset + 23, 37, 24);

        Button button_2 = new Button(shell, SWT.RADIO);
        button_2.setText("4");
        button_2.setBounds(265, heightOffset + 23, 37, 24);

        Button button_3 = new Button(shell, SWT.RADIO);
        button_3.setText("5");
        button_3.setBounds(308, heightOffset + 23, 37, 24);

        Button button_4 = new Button(shell, SWT.RADIO);
        button_4.setBounds(93, heightOffset + 23, 37, 24);
        button_4.setText("0");
    }

}
