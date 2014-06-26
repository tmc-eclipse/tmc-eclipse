package tmc.eclipse.spyware;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * 
 * Class that listens for editor changes. Basically we want to add listeners to
 * all editors; how we do this is by first adding a listener to the active
 * editor, then adding listeners as editors get activated. We also keep track of
 * the editors where we have already added a listener so that we do not add
 * listener again.
 * 
 * The listeners then will provide the plugin core with necessary data to log
 * text inserts\removals and paste events
 * 
 */
public class EditorListener implements IPartListener {

    private DocumentListener listener;
    private Set<IDocument> documentsWithListeners;

    /**
     * Constructor; inserts listener to active document as it does not receive
     * activation event when starting the IDE
     */
    public EditorListener() {
        // if eclipse disposes the associated IDocument, we want to get rid of
        // it as well so that we won't leak memory -> weakhashset
        documentsWithListeners = Collections.newSetFromMap(new WeakHashMap<IDocument, Boolean>());
        listener = new DocumentListener();
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();

        ITextEditor ite = (ITextEditor) activeEditor;
        if (activeEditor != null) {
            IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
            addListener(doc);
        }
    }

    /**
     * Method that is called when a part is activated; when this part is an
     * editor, we will add a listener to it
     * 
     * @param part
     *            Eclipse IDE part that contains activated part
     */
    @Override
    public void partActivated(IWorkbenchPart part) {
        if (part instanceof ITextEditor) {

            IDocument doc = ((ITextEditor) part).getDocumentProvider().getDocument(
                    ((ITextEditor) part).getEditorInput());
            addListener(doc);
        }
    }

    /**
     * Method that adds listener to document, if it does not already have one
     * 
     * @param doc
     *            The document where we want to inject the listener
     */
    private void addListener(IDocument doc) {
        if (documentsWithListeners.contains(doc)) {
            return;
        }
        doc.addDocumentListener(listener);
        documentsWithListeners.add(doc);
    }

    // don't really care about these methods
    @Override
    public void partBroughtToTop(IWorkbenchPart part) {

    }

    @Override
    public void partClosed(IWorkbenchPart part) {

    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {

    }

    @Override
    public void partOpened(IWorkbenchPart part) {

    }

}