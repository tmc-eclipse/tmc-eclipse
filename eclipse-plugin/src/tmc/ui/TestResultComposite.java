package tmc.ui;

import org.eclipse.swt.SWT;
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

    private int heightOffset;

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
        Composite moreDetails = new Composite(this, SWT.SMOOTH);
        heightOffset = 0;
        for (StackTraceElement st : tcr.getException().stackTrace) {
            if (st.isNativeMethod()) {
                addMoreDetailsLink(moreDetails, st.toString(), st);
            } else {
                addMoreDetailsLink(moreDetails, "<a href=\"\">" + st.toString() + "</a>", st);
                // addMoreDetailsLink(moreDetails,
                // "This is a link to <a href=\"http://www.google.com\">Google</a>");
            }
        }
        moreDetails.setBackground(BACKGROUND);
        moreDetails.setLocation(testResultMessage.getSize().x, heightOffset);
        moreDetails.setBounds(10, testResultMessage.getSize().y + 5, testResultMessage.getSize().x, heightOffset);

        colorBar.setBounds(0, 0, 5, testResultMessage.getSize().y + testResultName.getSize().y
                + moreDetails.getSize().y);
        if (this.getParent().getParent().getParent() instanceof TestRunnerComposite) {
            ((TestRunnerComposite) this.getParent().getParent().getParent()).enlargeTestStack(this, tcr);
        } else {
            System.out.println(this.getParent().getParent().getParent());
        }

        if (showMoreBtn != null) {
            showMoreBtn.dispose();
        }
    }

    public void addMoreDetailsLink(Composite parent, String text, StackTraceElement st) {
        Link moreDetailslink = new Link(parent, SWT.NONE);
        moreDetailslink.setText(text);
        moreDetailslink.setBackground(BACKGROUND);
        moreDetailslink.setLocation(testResultMessage.getSize().x, heightOffset);
        moreDetailslink.setBounds(0, heightOffset, testResultMessage.getSize().x, testResultMessage.getSize().y);
        final String path = "";
        moreDetailslink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("asd");
                // Open default external browser
                // IFileStore fileStore = EFS.getLocalFileSystem().getStore(new
                // File(path).toURI());
                // IWorkbenchPage page =
                // PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                // try {
                // IDE.openEditorOnFileStore(page, fileStore);
                // } catch (PartInitException e1) {
                // // TODO Auto-generated catch block
                // e1.printStackTrace();
                // }
            }
        });
        heightOffset += testResultMessage.getSize().y;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
