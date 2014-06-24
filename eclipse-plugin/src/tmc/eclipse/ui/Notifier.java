package tmc.eclipse.ui;

import org.eclipse.mylyn.commons.ui.dialogs.AbstractNotificationPopup;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import tmc.eclipse.handlers.CustomNotification;


public class Notifier {

    public void CreateNotification() {
        AbstractNotificationPopup notification = new CustomNotification(Display.getDefault(), "otsikko", "tässä on tekstiä");
        notification.open();
    }
}
