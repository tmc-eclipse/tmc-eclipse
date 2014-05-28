package tmc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class TestRunnerView extends ViewPart {

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

        final ScrolledComposite master = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        master.setLayout(gridLayout);
        master.setExpandHorizontal(true);
        master.setExpandVertical(true);
        final TestRunnerComposite comp = new TestRunnerComposite(master, SWT.SMOOTH);
        master.setContent(comp);
        master.setMinSize(500, 100);

        master.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(final ControlEvent e) {
                master.setSize(master.getParent().getSize().x, master.getParent().getSize().y - 5);
                comp.resize();

                // IJavaProject p2 = null;

                // IWorkspaceRoot workspaceRoot =
                // ResourcesPlugin.getWorkspace().getRoot();
                // IProject project = workspaceRoot.getProject("main");
                //
                // try {
                // p2 = JavaCore.create(project);
                // } catch (Throwable ex) {
                // }
                //
                // IProgressMonitor myProgressMonitor = new
                // NullProgressMonitor();
                // try {
                // ((IProject)
                // project).build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
                // myProgressMonitor);
                // launch(p2, "main");
                // } catch (CoreException ce) {
                // System.out.println();
                // ce.printStackTrace();
                // }
            }
        });

    }
    //
    // public void launch(IJavaProject proj, String main) throws CoreException {
    // IVMInstall vm = JavaRuntime.getVMInstall(proj);
    // if (vm == null)
    // vm = JavaRuntime.getDefaultVMInstall();
    // IVMRunner vmr = vm.getVMRunner(ILaunchManager.RUN_MODE);
    // String[] cp = JavaRuntime.computeDefaultRuntimeClassPath(proj);
    // VMRunnerConfiguration config = new VMRunnerConfiguration(main, cp);
    // ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
    // vmr.run(config, launch, null);
    // }
}
