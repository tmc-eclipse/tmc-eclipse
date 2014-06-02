package tmc.ui;

import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
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

                IJavaProject p2 = null;

                IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
                IProject project = workspaceRoot.getProject("arith_funcs");

                try {
                    p2 = JavaCore.create(project);
                } catch (Throwable ex) {
                }

                IProgressMonitor myProgressMonitor = new NullProgressMonitor();
                try {
                    ((IProject) project).build(IncrementalProjectBuilder.INCREMENTAL_BUILD, myProgressMonitor);
                    launch(p2, "arith_funcs");
                } catch (CoreException ce) {
                    System.out.println();
                    ce.printStackTrace();
                }
            }
        });

    }

    public void launch(IJavaProject proj, String main) throws CoreException {
        IVMInstall vm = JavaRuntime.getVMInstall(proj);
        if (vm == null)
            vm = JavaRuntime.getDefaultVMInstall();
        IVMRunner vmr = vm.getVMRunner(ILaunchManager.RUN_MODE);
        String[] cp = JavaRuntime.computeDefaultRuntimeClassPath(proj);

        for (int i = 0; i < cp.length; i++) {
            System.out.println(cp[i]);
        }
        Properties prop = System.getProperties();
        prop.setProperty("java.class.path", prop.getProperty("java.class.path", null));
        System.out.println("java.class.path now = " + prop.getProperty("java.class.path", null));
        System.out.println("--");

        VMRunnerConfiguration config = new VMRunnerConfiguration(main, cp);
        ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
        vmr.run(config, launch, null);
    }
}
