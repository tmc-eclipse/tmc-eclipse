package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

<<<<<<< HEAD
import tmc.testRunnerDomain.SubmissionResult;

public class TestRunnerView extends ViewPart {

	TestRunnerComposite comp = null;

	public TestRunnerView() {
		super();
	}

	public void setFocus() {
	}

	public void createPartControl(final Composite parent) {

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);

		final ScrolledComposite master = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		master.setLayout(gridLayout);
		master.setExpandHorizontal(true);
		master.setExpandVertical(true);
		comp = new TestRunnerComposite(master, SWT.SMOOTH);
		master.setContent(comp);
		master.setMinSize(500, 100);

		master.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				master.setSize(master.getParent().getSize().x, master
						.getParent().getSize().y - 5);
				comp.resize();
			}
		});
	}

	public void addSubmissionResult(SubmissionResult sr) {
		comp.addSubmissionResult(sr);
	}
=======
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;

public class TestRunnerView extends ViewPart {

    TestRunnerComposite comp = null;
    ScrolledComposite master = null;

    public TestRunnerView() {
        super();
    }

    public void setFocus() {
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
        master.setMinSize(500, 100);

        master.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(final ControlEvent e) {
                master.setSize(master.getParent().getSize().x, master.getParent().getSize().y - 5);
                comp.resize();
            }
        });
    }

    public void addSubmissionResult(SubmissionResult sr) {
        // if (comp != null) {
        comp.addSubmissionResult(sr);
        // }
    }
>>>>>>> 20785ab70a6714f94b116ee5ea936e753b8d2a63

}
