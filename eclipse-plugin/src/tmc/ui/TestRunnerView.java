package tmc.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;

public class TestRunnerView extends ViewPart {

    TestRunnerComposite comp = null;
    ScrolledComposite master = null;
    private Composite parent;

    public TestRunnerView() {
        super();
    }

    public void setFocus() {
        // Prevent this view from consuming all selection events
        // by assigning focus to the parent composite
        if (parent != null) {
            parent.setFocus();
        }
    }

    public void createPartControl(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);

        master = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        master.setLayout(gridLayout);
        master.setExpandHorizontal(true);
        master.setExpandVertical(true);
        comp = new TestRunnerComposite(master, SWT.SMOOTH);
        master.setContent(comp);
        master.setMinSize(100, 100);

        master.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(final ControlEvent e) {
                master.setSize(master.getParent().getSize().x, master.getParent().getSize().y - 5);
                comp.resize();
            }
        });

        this.parent = parent;
    }

    public void addSubmissionResult(List<TestCaseResult> tcr) {
        comp.addSubmissionResult(tcr);
    }
}
