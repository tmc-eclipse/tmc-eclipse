package tmc.spyware;

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

    public EditorListener() {
        // if eclipse disposes the associated IDocument, we want to get rid of
        // it as well
        documentsWithListeners = Collections.newSetFromMap(new WeakHashMap<IDocument, Boolean>());
        listener = new DocumentListener();
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();

        ITextEditor ite = (ITextEditor) activeEditor;
        IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
        addListener(doc);
    }

    @Override
    public void partActivated(IWorkbenchPart part) {
        if (part instanceof ITextEditor) {

            IDocument doc = ((ITextEditor) part).getDocumentProvider().getDocument(
                    ((ITextEditor) part).getEditorInput());
            addListener(doc);
        }
    }

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