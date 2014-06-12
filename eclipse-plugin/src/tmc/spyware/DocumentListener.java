package tmc.spyware;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.spyware.DocumentInfo;

public class DocumentListener implements IDocumentListener {

    @Override
    public void documentAboutToBeChanged(DocumentEvent event) {
        // System.out.println("Document about to be changed: " +
        // event.getText());
    }

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
