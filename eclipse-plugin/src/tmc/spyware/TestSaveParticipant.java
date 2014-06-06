package tmc.spyware;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.runtime.CoreException;

public class TestSaveParticipant implements ISaveParticipant {

	@Override
	public void doneSaving(ISaveContext context) {
		System.out.println("Done saving");
		
	}

	@Override
	public void prepareToSave(ISaveContext context) throws CoreException {
		System.out.println("Preparing to save");
		
	}

	@Override
	public void rollback(ISaveContext context) {
		System.out.println("Rollback");
	}

	@Override
	public void saving(ISaveContext context) throws CoreException {
		System.out.println("Saving");
	}



}
