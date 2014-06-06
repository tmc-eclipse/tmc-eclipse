package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;

public class TestResultComposite extends Composite {
    private Text colorBar;
    private Label testResultName;
    private Button showMoreBtn;
    private Label testResultMessage;
    private final Color PASS = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);

    private final Color FAIL = Display.getCurrent().getSystemColor(SWT.COLOR_RED);

    private final Color BACKGROUND = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

    private int colorBarHeight;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public TestResultComposite(Composite parent, int style, final TestCaseResult tcr) {
        super(parent, style);
        setBackground(BACKGROUND);

        final GC gc = new GC(Display.getDefault());

        testResultName = new Label(this, SWT.SMOOTH);
        testResultName.setBackground(BACKGROUND);
        testResultName.setText(tcr.getName());
        testResultName.setForeground(PASS);
        testResultName.setFont(new Font(Display.getCurrent(), "Arial", 12, SWT.BOLD));

        Point i = gc.stringExtent(testResultName.getText());

        testResultName.setBounds(10, 0, parent.getSize().x, i.y + 5);

        colorBar = new Text(this, SWT.READ_ONLY);
        colorBar.setBounds(0, 0, 5, testResultName.getSize().y);
        colorBar.setBackground(PASS);
        if (!tcr.isSuccessful()) {
            testResultName.setForeground(FAIL);

            testResultMessage = new Label(this, SWT.SMOOTH);
            testResultMessage.setBackground(BACKGROUND);
            testResultMessage.setText(tcr.getMessage());

            i = gc.stringExtent(testResultMessage.getText());

            testResultMessage.setBounds(10, testResultName.getSize().y, parent.getSize().x, i.y);

            showMoreBtn = new Button(this, SWT.SMOOTH);
            showMoreBtn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    showMoreDetails(tcr, gc);
                }
            });

            showMoreBtn.setBounds(10, testResultMessage.getSize().y + 25, 170, 29);
            showMoreBtn.setText("Show detailed message");

            colorBarHeight = testResultName.getSize().y + testResultMessage.getSize().y + showMoreBtn.getSize().y + 20;

            colorBar.setBounds(0, 0, 5, colorBarHeight);

            colorBar.setBackground(FAIL);

        }

    }

    public void setTestResultName(String message) {
        testResultName.setText(message);
    }

    public void setTestResultMessage(String message) {
        testResultMessage.setText(message);
    }

    public Text getColorBar() {
        return colorBar;
    }

    public Label getTestResultName() {
        return testResultName;
    }

    private void showMoreDetails(TestCaseResult tcr, GC gc) {
        final Link moreDetails = new Link(this, SWT.NONE);
        int i = 0;
        StringBuilder b = new StringBuilder();
        for (StackTraceElement st : tcr.getException().stackTrace) {
            if (st.isNativeMethod()) {
                b.append(st.toString());
            } else {
                b.append("<a>" + st.toString() + "</a>");
            }
            b.append("\n");
            i++;
        }
        moreDetails.setBackground(BACKGROUND);
        moreDetails.setText(b.toString());
        moreDetails.setBounds(10, testResultMessage.getSize().y + 5, testResultMessage.getSize().x,
                gc.stringExtent(moreDetails.getText()).y * i);
        moreDetails.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDown(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseUp(MouseEvent e) {
                String[] links = moreDetails.getText().split("<a>");
            }

        });

        colorBar.setBounds(0, 0, 5, moreDetails.getSize().y);
        if (this.getParent().getParent().getParent() instanceof TestRunnerComposite) {
            ((TestRunnerComposite) this.getParent().getParent().getParent()).enlargeTestStack(this, tcr);
        } else {
            System.out.println(this.getParent().getParent().getParent());
        }

        if (showMoreBtn != null) {
            showMoreBtn.dispose();
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
