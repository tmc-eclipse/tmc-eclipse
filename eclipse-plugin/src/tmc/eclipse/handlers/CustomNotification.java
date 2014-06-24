package tmc.eclipse.handlers;

import org.eclipse.mylyn.commons.ui.dialogs.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class CustomNotification extends AbstractNotificationPopup {

    public CustomNotification(Display display, String title, String text) {
        super(display, SWT.ON_TOP);
    }

}
