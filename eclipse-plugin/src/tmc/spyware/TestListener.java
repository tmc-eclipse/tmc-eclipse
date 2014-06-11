package tmc.spyware;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class TestListener implements Listener {
    public static boolean installListeners = false;

    @Override
    public void handleEvent(Event event) {

        if (installListeners) {
            installListeners = false;

            for (IEditorReference r : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getEditorReferences()) {
                ITextEditor e;

                e = ((ITextEditor) r.getEditor(true));

                e.getDocumentProvider().getDocument(e.getEditorInput()).addDocumentListener(new DocumentListener());
            }

        }

        switch (event.type) {
        case SWT.Modify:
            // System.out.println("SWT.Modify!");
            // System.out.println("Character: " + event.character);
            // System.out.println("index: " + event.index);
            // System.out.println("Start: " + event.start);
            // System.out.println("end: " + event.end);
            // System.out.println("Text: " + event.text);
            // System.out.println("Detail: " + event.detail);
            // System.out.println("Count: " + event.count);
            // System.out.println("Item: " + event.item);
            // System.out.println("ToString: " + event.toString());

        case SWT.Verify:

            // System.out.println("SWT.Verify!");
            // System.out.println("Character: " + event.character);
            // System.out.println("index: " + event.index);
            // System.out.println("Start: " + event.start);
            // System.out.println("end: " + event.end);
            // System.out.println("Text: " + event.text);
            // System.out.println("Detail: " + event.detail);
            // System.out.println("Count: " + event.count);
            // System.out.println("Item: " + event.item);
            // System.out.println("ToString: " + event.toString());

        }

    }

}
