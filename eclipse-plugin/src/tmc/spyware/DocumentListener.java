package tmc.spyware;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

public class DocumentListener implements IDocumentListener {

    @Override
    public void documentAboutToBeChanged(DocumentEvent event) {
        // System.out.println("Document about to be changed: " +
        // event.getText());
    }

    @Override
    public void documentChanged(DocumentEvent event) {

        System.out.println("Document changed: *" + event.getText() + "*");
        System.out.println("Length: " + event.getLength());
        System.out.println("Offset: " + event.getOffset());
        // System.out.println("Document changed: *" + event.getText() +
        // "*");

    }
}
