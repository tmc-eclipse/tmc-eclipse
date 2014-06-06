package tmc.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;

public class TestRunnerComposite extends Composite {

    private final int PROGRESS_BAR_MULTIPLIER = 3;

    private ScrolledComposite scrolledComposite;
    private ProgressBar progressBar;
    private Label lblTestspassed;

    private int howManyTestsPassedPercent;
    private double howManyTestsPassedCount = 0;
    private double howManyTestsRan = 0;

    private List<TestCaseResult> results;
    private boolean showAllTests = false;

    private int heightOffset = 0;
    private Composite parent;
    private int style;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public TestRunnerComposite(final Composite parent, int style) {
        super(parent, style);
        this.parent = parent;
        this.style = style;
        makeScrolledComposite(parent);
    }

    private void createTestRunnerComposite(final Composite parent, int style) {

        scrolledComposite.dispose();

        this.setSize(parent.getSize().x, parent.getSize().y);

        progressBar = new ProgressBar(this, SWT.SMOOTH);
        progressBar.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
        progressBar.setMinimum(0);
        progressBar.setMaximum(300);
        progressBar.setBounds(19, 33, 300, 30);

        progressBar.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {

                Rectangle rectRed = parent.getShell().getClientArea();
                e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));
                e.gc.fillRectangle(rectRed);

                Rectangle rectGreen = new Rectangle(parent.getShell().getClientArea().x, parent.getShell()
                        .getClientArea().y, progressBar.getSelection(), parent.getShell().getClientArea().height);
                e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
                e.gc.fillRectangle(rectGreen);

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
                scrolledComposite.dispose();
                makeScrolledComposite(parent);
                showTestResults();
            }
        });

        makeScrolledComposite(parent);

        lblTestspassed = new Label(this, SWT.NONE);
        lblTestspassed.setBounds(19, 10, 183, 17);

        updateProgress();

    }

    private void makeScrolledComposite(Composite parent) {
        scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setSize(parent.getSize().x - 20, parent.getSize().y - 80);
        scrolledComposite.setLocation(10, 70);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setMinSize(0, 0);
        scrolledComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
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

    public void addSubmissionResult(List<TestCaseResult> tcr) {
        createTestRunnerComposite(this.parent, this.style);
        results = tcr;
        showTestResults();
    }

    private void showTestResults() {
        if (results == null) {
            return;
        }
        heightOffset = 0;
        howManyTestsPassedCount = 0;
        howManyTestsRan = results.size();
        Composite c = new Composite(scrolledComposite, SWT.BORDER);
        c.setSize(scrolledComposite.getSize());
        c.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        for (TestCaseResult tcr : results) {

            if (tcr.isSuccessful()) {
                ++howManyTestsPassedCount;
            }

            if (!showAllTests && tcr.isSuccessful()) {
                continue;
            }

            addTestResult(tcr, c);
        }
        scrolledComposite.setContent(c);
        updateProgress();

    }

    private void addTestResult(TestCaseResult tcr, Composite c) {
        TestResultComposite comp = new TestResultComposite(c, SWT.SMOOTH, tcr);
        int height = comp.getColorBar().getSize().y;
        scrolledComposite.setMinHeight(scrolledComposite.getMinHeight() + height);
        comp.setBounds(0, heightOffset, scrolledComposite.getClientArea().width, height);
        heightOffset += height;
    }

    public void enlargeTestStack(TestResultComposite comp, TestCaseResult tcr) {

        Composite composite = new Composite(scrolledComposite, SWT.BORDER);
        composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        composite.setSize(scrolledComposite.getSize());

        int height = comp.getColorBar().getSize().y;

        comp.setBounds(0, comp.getLocation().y, scrolledComposite.getClientArea().width, height);

        Control[] c = ((Composite) scrolledComposite.getChildren()[0]).getChildren();

        boolean found = false;
        int newHeightOffset = 0;
        int j = 0;

        while (!comp.equals(c[j])) {
            System.out.println(j);
            newHeightOffset += c[j].getSize().y;
            j++;
        }

        newHeightOffset += comp.getSize().y;
        for (int i = 0; i < c.length; i++) {

            c[i].setParent(composite);
            if (comp.equals(c[i])) {
                found = true;
            }
            if (found && comp != c[i]) {
                c[i].setBounds(0, newHeightOffset, scrolledComposite.getClientArea().width, c[i].getSize().y);
                newHeightOffset += c[i].getSize().y;
            }

        }
        scrolledComposite.setMinHeight(newHeightOffset);
        scrolledComposite.setContent(composite);
        scrolledComposite.getChildren()[0].dispose();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
