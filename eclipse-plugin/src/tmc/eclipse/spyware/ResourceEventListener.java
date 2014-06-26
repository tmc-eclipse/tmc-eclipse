package tmc.eclipse.spyware;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

import tmc.eclipse.spyware.EventDataVisitor.EventData;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.spyware.SnapshotInfo;

/**
 * 
 * Handles the rename\save\move etc. events for spyware and project database.
 * Event contains affected files in a tree; root and inner nodes are various
 * directories and leaf nodes contain the affected files
 */
public class ResourceEventListener implements IResourceChangeListener {

    @Override
    public void resourceChanged(IResourceChangeEvent event) {

        // pre_delete-event is used to update project file database
        if (event.getType() == IResourceChangeEvent.PRE_DELETE) {
            if (event.getResource() != null && event.getResource().getLocation() != null) {
                String path = event.getResource().getLocation().toString();
                Core.getProjectEventHandler().handleDeletion(path);
            }
            return;
        }

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

        for (EventData data : visitor.getEvents()) {

            SnapshotInfo snapshot = new SnapshotInfo(visitor.getProjectName(), data.oldPath, data.currentPath,
                    data.fullOldPath, data.fullCurrentPath, data.type);

            if (!snapshot.pathsAreEmpty()) {
                Core.getSpyware().takeSnapshot(snapshot);
                Core.getProjectEventHandler().handleSnapshot(snapshot);
            }
        }

    }
}
