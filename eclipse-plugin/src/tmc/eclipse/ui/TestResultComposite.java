package tmc.eclipse.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

import tmc.eclipse.activator.CoreInitializer;
import tmc.eclipse.util.WorkbenchHelper;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.TestCaseResult;

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
    private int howManyRows = 1;

    private WorkbenchHelper helper;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public TestResultComposite(Composite parent, int style, final TestCaseResult tcr) {
        super(parent, style);

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
            if (st.getFileName().toLowerCase().contains("test.")) {
                addMoreDetailsLink(moreDetails, "<a href=\"\">" + st.toString() + "</a>", st);
            } else {
                addMoreDetailsLink(moreDetails, st.toString(), st);
            }
        }

        moreDetails.setBackground(BACKGROUND);
        moreDetails.setLocation(testResultMessage.getSize().x, heightOffset);
        moreDetails.setBounds(10, testResultMessage.getSize().y + 5, testResultMessage.getSize().x, heightOffset);

        colorBar.setBounds(0, 0, 5, testResultMessage.getSize().y + testResultName.getSize().y
                + moreDetails.getSize().y);

        if (getGreatGreatGrandParent() instanceof TestRunnerComposite) {
            ((TestRunnerComposite) getGreatGreatGrandParent()).enlargeTestStack(this, tcr);
        }

        if (showMoreBtn != null) {
            showMoreBtn.dispose();
        }
    }

    public void addMoreDetailsLink(Composite parent, String text, final StackTraceElement st) {
        Link moreDetailslink = new Link(parent, SWT.NONE);
        moreDetailslink.setText(text);
        moreDetailslink.setBackground(BACKGROUND);
        moreDetailslink.setLocation(testResultName.getSize().x, heightOffset);
        moreDetailslink.setBounds(0, heightOffset, testResultName.getSize().x, testResultName.getSize().y);

        final StringBuilder path = new StringBuilder();

        String className = st.getClassName().replace('.', '/');

        path.append(getProjectTestRootPath(className) + "/");

        moreDetailslink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                System.out.println(st.getFileName());
                System.out.println(st.getClassName());
                System.out.println(path);

                final IFile inputFile = ResourcesPlugin.getWorkspace().getRoot()
                        .getFileForLocation(Path.fromOSString(path.toString().trim()));

                if (inputFile != null) {
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    IEditorPart openEditor = null;

                    try {
                        openEditor = IDE.openEditor(page, inputFile);
                    } catch (PartInitException e1) {
                        e1.printStackTrace();
                    }

                    int line = st.getLineNumber();

                    if (openEditor instanceof ITextEditor) {
                        ITextEditor textEditor = (ITextEditor) openEditor;
                        IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
                        try {
                            textEditor.selectAndReveal(document.getLineOffset(line - 1),
                                    document.getLineLength(line - 1));
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

            }
        });
        heightOffset += 20;
    }

    private String getProjectTestRootPath(String path) {
        Project project = helper.getActiveProject();
        if (project == null) {
            return "";
        } else {
            return project.getProjectFileByName(path);
        }
    }

    /*
     * Purpose of this method is to find the TestrunnerComposite so that its
     * methods can be used. We can't create a new TestrunnerComposite as we
     * wouldn't do anything with it without drastic changes to current parent
     * and its parents.
     */
    public Composite getGreatGreatGrandParent() {
        return this.getParent().getParent().getParent().getParent();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
