package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

public class TestRunnerComposite extends Composite {

    private final int PROGRESS_BAR_MULTIPLIER = 7;

    private ScrolledComposite scrolledComposite;
    private ProgressBar progressBar;
    private Label lblTestspassed;
    private int howManyTestsPassedPercent;
    private double howManyTestsPassedCount = 4;
    private double howManyTestsRan = 8;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public TestRunnerComposite(Composite parent, int style) {
        super(parent, style);

        howManyTestsPassedPercent += (howManyTestsPassedCount / howManyTestsRan) * 100;

        progressBar = new ProgressBar(this, SWT.SMOOTH);
        progressBar.setToolTipText("");
        progressBar.setBackground(Display.getDefault().getSystemColor(5));
        progressBar.setMinimum(0);
        progressBar.setMaximum(700);
        progressBar.setBounds(19, 33, 300, 30);

        Button btnShowAllTests = new Button(this, SWT.CHECK);
        btnShowAllTests.setBounds(325, 33, 128, 24);
        btnShowAllTests.setText("Show all tests");

        scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setSize(680, 220);
        scrolledComposite.setMinSize(680, 220);
        scrolledComposite.setLocation(10, 69);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setBackground(Display.getCurrent().getSystemColor(1));

        lblTestspassed = new Label(this, SWT.NONE);
        lblTestspassed.setBounds(19, 10, 183, 17);
        lblTestspassed.setText("TestsPassed: " + howManyTestsPassedPercent + "%");

        updateProgress();

    }

    public void resize() {
        scrolledComposite.setSize(this.getParent().getSize().x - 20, this.getParent().getSize().y - 80);
        scrolledComposite.redraw();
        this.update();

    }

    private void updateProgress() {
        progressBar.setSelection(howManyTestsPassedPercent * PROGRESS_BAR_MULTIPLIER);
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
