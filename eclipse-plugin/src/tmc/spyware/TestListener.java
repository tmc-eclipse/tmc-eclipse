package tmc.spyware;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class TestListener implements Listener {

	@Override
	public void handleEvent(Event event) {
		System.out.println("Eventti havaittu!");
		switch (event.type) {
		case SWT.Modify:
			System.out.println(getEditorData());
			break;

		}

	}

	/* Test code please ignore */
	private String getEditorData() {

	
		
		final IEditorPart activeEditor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor == null)
			return "active editor null";


	    IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput() ;
	    IFile file = input.getFile();
	    IProject activeProject = file.getProject();
	    String activeProjectName = activeProject.getName();
		
		if (!(activeEditor instanceof ITextEditor))
			return "Ei ITextEditor";
		ITextEditor ite = (ITextEditor) activeEditor;
			
		IDocument doc = ite.getDocumentProvider().getDocument(
				ite.getEditorInput());
		if (doc == null)
			return "doc null";

		return activeProjectName + "\n" + activeEditor.getTitle() + "\n" + doc.get();

	}

}
