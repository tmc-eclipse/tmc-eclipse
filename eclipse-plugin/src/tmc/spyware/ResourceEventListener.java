package tmc.spyware;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.spyware.SnapshotInfo;

/**
 * 
 * Handles the rename\save\move etc. events for spyware. Event contains affected
 * files in a tree; root and inner nodes are various directories and leaf nodes
 * contain the affected files
 */
public class ResourceEventListener implements IResourceChangeListener {

    @Override
    public void resourceChanged(IResourceChangeEvent event) {

        if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
            return;
        }

        // visitor visits the tree nodes
        EventDataVisitor visitor = new EventDataVisitor();
        try {
            event.getDelta().accept(visitor);
        } catch (CoreException e) {
            return;
        }

        Core.getSpyware().takeSnapshot(
                new SnapshotInfo(visitor.getProjectName(), visitor.getOldPath(), visitor.getCurrentPath(),
                        visitor.getCurrentRelativePath(), visitor.getType()));
    }

    private String getEditorData() {

        IWorkbenchWindow activeWorkbench = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbench == null) {
            return "";
        }

        final IEditorPart activeEditor = activeWorkbench.getActivePage().getActiveEditor();

        if (activeEditor == null) {
            return "";
        }

        if (!(activeEditor instanceof ITextEditor)) {
            return "";
        }

        ITextEditor ite = (ITextEditor) activeEditor;

        IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
        if (doc == null) {
            return "";
        }

        return doc.get();

    }
}
