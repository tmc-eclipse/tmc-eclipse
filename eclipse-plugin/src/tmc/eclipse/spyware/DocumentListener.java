package tmc.eclipse.spyware;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.spyware.DocumentInfo;

/**
 * This class listens to editor events such as text inserts. Basically this is
 * the keylogging component of the spyware. Any actual processing happens in the
 * core
 */
public class DocumentListener implements IDocumentListener {

    @Override
    public void documentAboutToBeChanged(DocumentEvent event) {
    }

    /**
     * Method that is called when the listened document is changed
     * 
     * @param event
     *            The event received from Eclipse
     */
    @Override
    public void documentChanged(DocumentEvent event) {

        final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();

        IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput();

        IFile file = input.getFile();

        Core.getSpyware().documentChange(
                new DocumentInfo(file.getLocation().toString(), file.getFullPath().toString(), event.getDocument()
                        .get(), event.getText(), event.getOffset(), event.getLength()));

    }
}
