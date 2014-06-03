package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;

public class TestRunnerComposite extends Composite {

    private final int PROGRESS_BAR_MULTIPLIER = 7;

    private ScrolledComposite scrolledComposite;
    private ProgressBar progressBar;
    private Label lblTestspassed;
    private Button btnShowAllTests;
    private Text resultText;

    private int howManyTestsPassedPercent;
    private double howManyTestsPassedCount = 4;
    private double howManyTestsRan = 8;

    private SubmissionResult result;
    private boolean showAllTests = false;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public TestRunnerComposite(Composite parent, int style) {
        super(parent, style);

        progressBar = new ProgressBar(this, SWT.SMOOTH);
        progressBar.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
        progressBar.setMinimum(0);
        progressBar.setMaximum(700);
        progressBar.setBounds(19, 33, 300, 30);

        progressBar.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                Point widgetSize = progressBar.getSize();
                int percentage = progressBar.getSelection() / PROGRESS_BAR_MULTIPLIER;
                String text = percentage + "%";
                Point textSize = e.gc.stringExtent(text);
                e.gc.setForeground(progressBar.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                e.gc.drawString(text, ((widgetSize.x - textSize.x) / 2), ((widgetSize.y - textSize.y) / 2), true);
            }
        });

        final Button btnShowAllTests = new Button(this, SWT.CHECK);
        btnShowAllTests.setBounds(325, 33, 128, 24);
        btnShowAllTests.setText("Show all tests");
        btnShowAllTests.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnShowAllTests.getSelection()) {
                    showAllTests = true;
                } else {
                    showAllTests = false;
                }
                showTestResults();
            }
        });

        scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setSize(680, 220);
        scrolledComposite.setMinSize(680, 220);
        scrolledComposite.setLocation(10, 69);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

        lblTestspassed = new Label(this, SWT.NONE);
        lblTestspassed.setBounds(19, 10, 183, 17);

        resultText = new Text(scrolledComposite, SWT.SMOOTH | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);

        updateProgress();

    }

    public void resize() {
        scrolledComposite.setSize(this.getParent().getSize().x - 20, this.getParent().getSize().y - 80);
        scrolledComposite.redraw();
        this.update();

    }

    private void updateProgress() {
        howManyTestsPassedPercent = (int) ((howManyTestsPassedCount / howManyTestsRan) * 100);
        lblTestspassed.setText("Tests passed: " + (int) howManyTestsPassedCount);
        progressBar.setSelection(howManyTestsPassedPercent * PROGRESS_BAR_MULTIPLIER);
    }

    public void addSubmissionResult(SubmissionResult sr) {

        result = sr;
        showTestResults();
    }

    private void showTestResults() {
        if (result == null) {
            return;
        }
        howManyTestsPassedCount = 0;
        howManyTestsRan = result.getTestCases().size();

        StringBuilder b = new StringBuilder();
        for (TestCaseResult tcr : result.getTestCases()) {

            if (tcr.isSuccessful()) {
                ++howManyTestsPassedCount;
            }

            if (!showAllTests && tcr.isSuccessful()) {
                continue;
            }

            b.append(tcr.getName());
            b.append("\n");
            b.append(tcr.getMessage());
            b.append("\n\n");

        }

        resultText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        resultText.setSize(resultText.getParent().getSize().x, resultText.getParent().getSize().y);
        resultText.setText(b.toString());
        updateProgress();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
