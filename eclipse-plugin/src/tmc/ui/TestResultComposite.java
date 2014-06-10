package tmc.ui;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import tmc.activator.CoreInitializer;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.domain.Project;
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

    private WorkbenchHelper helper;
    int howManyRows = 1;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public TestResultComposite(Composite parent, int style, final TestCaseResult tcr) {
        super(parent, style);

        howManyRows = 1;

        this.helper = CoreInitializer.getDefault().getWorkbenchHelper();

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

            howManyRows = tcr.getMessage().split("\n").length;
            i = gc.stringExtent(testResultMessage.getText());

            testResultMessage.setBounds(10, testResultName.getSize().y, parent.getSize().x, i.y * howManyRows);

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
            if (!st.getClassName().toLowerCase().contains("test") || st.getClassName().contains("fi.helsinki.cs.")) {
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
        if (getGreatGreatGrandParent() instanceof TestRunnerComposite) {
            ((TestRunnerComposite) getGreatGreatGrandParent()).enlargeTestStack(this, tcr);
        } else {
            System.out.println("Not the right parent!!!");
        }

        if (showMoreBtn != null) {
            showMoreBtn.dispose();
        }
    }

    public void addMoreDetailsLink(Composite parent, String text, final StackTraceElement st) {
        Link moreDetailslink = new Link(parent, SWT.NONE);
        moreDetailslink.setText(text);
        moreDetailslink.setBackground(BACKGROUND);

        // PlatformUI.getWorkbench().getActiveWorkbenchWindow().openPage(perspectiveId,
        // input)

        moreDetailslink.setLocation(testResultName.getSize().x, heightOffset);
        moreDetailslink.setBounds(0, heightOffset, testResultName.getSize().x, testResultName.getSize().y);

        final StringBuilder path = new StringBuilder();

        path.append(getProjectRootPath() + "/");
        if (!st.getClassName().contains("test.")) {
            path.append("test/");
        }
        path.append(st.getClassName().replace('.', '/') + ".java");

        moreDetailslink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println(path.toString());
                System.out.println(st.getClassName());
                System.out.println(st.getFileName());
                System.out.println(st.getMethodName());

                File fileToOpen = new File(path.toString());

                if (fileToOpen.exists() && fileToOpen.isFile()) {
                    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
                    System.out.println(fileStore.toURI().toString());
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

                    try {
                        IDE.openEditorOnFileStore(page, fileStore);
                    } catch (PartInitException e1) {
                        // Put your exception handler here if you wish to
                    }
                } else {
                    // DO SOMETHING IF THE FILE DOES NOT EXIST
                }

            }
        });
        heightOffset += 20;
    }

    private String getProjectRootPath() {
        Project project = helper.getActiveProject();
        if (project == null) {
            return "";
        } else {
            return project.getRootPath();
        }
    }

    /*
     * Purpose of this method is to find the TestrunnerComposite so that its
     * methods can be used. We can't create a new TestrunnerComposite as we
     * wouldn't do anything with it without drastic changes to current and its
     * parents.
     */
    public Composite getGreatGreatGrandParent() {
        return this.getParent().getParent().getParent().getParent();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
