package tmc.spyware;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TestKeyListener implements Listener {

    @Override
    public void handleEvent(Event event) {

        switch (event.type) {
        case SWT.Modify:
            // System.out.println(getEditorData());
            break;

        }
    }

}
