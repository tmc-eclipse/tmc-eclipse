package tmc.ui;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import tmc.tasks.TaskStarter;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackQuestion;

public class SuccesfulSubmitDialog extends Dialog {

    private static final int NULL_OFFSET = 1;
    private static final String DEFAULT_FONT = "Ubuntu";

    private static final int TEXT_FIELD_CHAR_LENGTH = 10;
    private static final int TEXT_FIELD_MIN_SIZE = 10;

    public static final int QUESTIONS_HEIGHT_OFFSET = 150;
    public static final int RATING_QUESTION_HEIGHT = 60;
    public static final int TEXT_QUESTION_HEIGHT = 110;

    protected Object result;
    protected Shell shell;

    private List<String> pointsAwarded;
    private String feedbackUrl;
    private String modelSolutionUrl;

    private List<FeedbackQuestion> questions;
    private List<FeedbackAnswer> answers;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public SuccesfulSubmitDialog(Shell parent, String title) {
        super(parent, SWT.SHEET);

        questions = new ArrayList<FeedbackQuestion>();
        answers = new ArrayList<FeedbackAnswer>();

        pointsAwarded = new ArrayList<String>();
        modelSolutionUrl = "";

        setText(title);
    }

    public void setPointsAwarded(List<String> pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public void SetFeedbackUrl(String url) {
        this.feedbackUrl = url;
    }

    public void setModelSolutionUrl(String modelSolutionUrl) {
        this.modelSolutionUrl = modelSolutionUrl;
    }

    public void addFeedbackQuestion(FeedbackQuestion question) {
        this.questions.add(question);
    }

    public List<FeedbackAnswer> getFeedbackAnswers() {
        return answers;
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

        Label testStatusLabel = new Label(shell, SWT.NONE);
        testStatusLabel.setFont(SWTResourceManager.getFont(DEFAULT_FONT, 12, SWT.NORMAL));
        testStatusLabel.setText("All tests passed on the server.");
        testStatusLabel.setForeground(SWTResourceManager.getColor(34, 139, 34));
        testStatusLabel.setBounds(53, 10, 301, 37);

        Label imageLabel = new Label(shell, SWT.NONE);
        imageLabel.setImage(ResourceManager.getPluginImage("eclipse-plugin", "icons/smile.gif"));
        imageLabel.setBounds(10, 0, 37, 47);

        Label pointsAwardedLabel = new Label(shell, SWT.NONE);
        pointsAwardedLabel.setBounds(10, 53, 344, 17);
        pointsAwardedLabel.setText(getPointsAwardedMessage());

        Button viewModelSolutionButton = new Button(shell, SWT.NONE);
        viewModelSolutionButton.setBounds(10, 76, 163, 29);
        viewModelSolutionButton.setText("View model solution");
        viewModelSolutionButton.addSelectionListener(new SelectionAdapter() {
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

        Button closeButton = new Button(shell, SWT.NONE);
        closeButton.setText("OK");
        closeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TaskStarter.startFeedbackSubmissionTask(answers, feedbackUrl);
                shell.close();
            }

        });
        closeButton.setBounds(290, 10 + heightOffset, 64, 29);

    }

    private String getPointsAwardedMessage() {
        StringBuilder b = new StringBuilder();
        b.append("Points permanently awarded:");
        for (String point : pointsAwarded) {
            b.append(" " + point);
        }

        b.append(".");
        return b.toString();
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

        Label feedbackLabel = new Label(shell, SWT.NONE);
        feedbackLabel.setBounds(10, 118, 250, 17);
        feedbackLabel.setText("Feedback (leave empty to not send):");

        for (FeedbackQuestion question : questions) {
            if (question.isIntRange()) {
                addRatingFeedbackField(question.getId(), question.getQuestion(), heightOffset,
                        question.getIntRangeMin(), question.getIntRangeMax());
                heightOffset += RATING_QUESTION_HEIGHT;
            }
            if (question.isText()) {
                addTextFeedbackField(question.getId(), question.getQuestion(), heightOffset);
                heightOffset += TEXT_QUESTION_HEIGHT;
            }
            answers.add(new FeedbackAnswer(question));
        }

        return heightOffset;
    }

    private void addTextFeedbackField(final int id, String question, int heightOffset) {
        Label label = new Label(shell, SWT.NONE);
        label.setText(question);
        label.setFont(SWTResourceManager.getFont(DEFAULT_FONT, 10, SWT.NORMAL));
        label.setBounds(26, heightOffset, 325, 17);

        final Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        text.setBounds(29, heightOffset + 23, 325, 72);

        text.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                updateAnswer(id, text.getText());
            }

        });
    }

    private void addRatingFeedbackField(final int id, String question, int heightOffset, final int minValue,
            final int maxValue) {

        Label label = new Label(shell, SWT.NONE);
        label.setFont(SWTResourceManager.getFont(DEFAULT_FONT, 10, SWT.NORMAL));
        label.setText(question);
        label.setBounds(26, heightOffset, 325, 17);

        final Scale scale = new Scale(shell, SWT.HORIZONTAL);

        scale.setSelection(minValue);
        scale.setMinimum(minValue);
        scale.setMaximum(maxValue + NULL_OFFSET);

        scale.setIncrement(1);
        scale.setPageIncrement(1);
        scale.setBounds(26, heightOffset + 20, 250, 24);

        final Text text = new Text(shell, SWT.BORDER);
        text.setText("");

        int textFieldDigits = (int) Math.ceil(Math.log10(maxValue));

        text.setBounds(280, heightOffset + 20, (TEXT_FIELD_CHAR_LENGTH * textFieldDigits) + TEXT_FIELD_MIN_SIZE, 24);
        text.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                try {
                    if (Integer.parseInt(text.getText()) >= minValue - 1
                            && Integer.parseInt(text.getText()) <= (maxValue)) {
                        scale.setSelection(Integer.parseInt(text.getText()) + 1);
                    } else {
                        scale.setSelection(minValue);
                        text.setText("");
                    }

                    updateAnswer(id, scale.getSelection() - NULL_OFFSET + "");
                } catch (Exception e1) {
                }

            }

        });

        scale.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (scale.getSelection() == minValue) {
                    text.setText("");
                } else {
                    text.setText("" + (scale.getSelection() - 1));
                }
                text.setFocus();
                text.selectAll();

                updateAnswer(id, scale.getSelection() - NULL_OFFSET + "");
            }

        });

    }

    protected void updateAnswer(int id, String value) {
        for (FeedbackAnswer answer : answers) {
            if (answer.getQuestion().getId() == id) {
                answer.setAnswer(value);
            }
        }
    }

    public void setFeedbackQuestions(List<FeedbackQuestion> feedbackQuestions) {
        questions = feedbackQuestions;
    }
}
