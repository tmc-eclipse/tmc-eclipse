package tmc.ui;

import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

public class TestRunnerDialog extends ViewPart {
	
        private Label label;
        
        public TestRunnerDialog() {
                super();
        }
        
        public void setFocus() {
                label.setFocus();
        }
        
        public void createPartControl(Composite parent) {
                label = new Label(parent, 0);
                label.setText("Hello ");
        }

}
