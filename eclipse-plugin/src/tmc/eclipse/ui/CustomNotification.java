package tmc.eclipse.ui;

import org.eclipse.mylyn.commons.ui.dialogs.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

public class CustomNotification extends AbstractNotificationPopup {
    private String title;
    private String text;
    private Listener listener;
    private boolean isAlive;

    public CustomNotification(Display display, String title, String text, Listener listener) {
        super(display, SWT.ON_TOP);
        this.title = title;
        this.text = text;
        this.listener = listener;
        setDelayClose(1000 * 3600 * 24 * 365);
        this.isAlive = true;
    }

  
    
    @Override
    protected String getPopupShellTitle() {
        return title;
    }

    @Override
    protected void createContentArea(Composite composite) {
        Label label = new Label(composite, SWT.WRAP);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Link link = new Link(composite, 0);
        link.setText("<a>" + text + "</a>");
        composite.addListener(SWT.Activate, listener);
        composite.addListener(SWT.Activate, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
               close();
               isAlive = false;
            }
        });
        composite.addListener(SWT.Dispose, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
               close();
               isAlive = false;
            }
        });
    }

    @Override
    protected void createTitleArea(Composite composite) {
        super.createTitleArea(composite);
        
    }
    
    public boolean isAlive() {
        return isAlive;
    }
}
