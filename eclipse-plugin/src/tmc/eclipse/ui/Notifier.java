package tmc.eclipse.ui;

import org.eclipse.mylyn.commons.ui.dialogs.AbstractNotificationPopup;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class Notifier {
    
//    Usage: 
//    Notifier.CreateNotification("Title", "Content", new Listener() {
//        public void handleEvent(Event e) {
//            System.out.println("click!");
//        }
//    });
//    
    
    public static void CreateNotification(String title, String text, Listener listener) {
        AbstractNotificationPopup notification = new CustomNotification(Display.getDefault(), title, text, listener);
        notification.open();
    }
}
