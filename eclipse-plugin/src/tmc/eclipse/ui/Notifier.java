package tmc.eclipse.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;


public class Notifier {
    private static Notifier notifier;
    
//    Usage: 
//    Notifier.getInstance().CreateNotification("Title", "Content", new Listener() {
//        public void handleEvent(Event e) {
//            System.out.println("click!");
//        }
//    });
//    
    
    public CustomNotification createNotification(String title, String text, Listener listener) {
        CustomNotification notification = new CustomNotification(Display.getDefault(), title, text, listener);
        notification.open();
        return notification;
    }
    
    public static Notifier getInstance() {
        if (notifier == null) {
            return new Notifier();
        }
        return notifier;
    }
}
