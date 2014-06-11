package tmc.spyware;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

import tmc.spyware.EventDataVisitor.EventData;
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

        for (EventData data : visitor.getEvents()) {
            // System.out.println("visitor.getOldPath(): " + data.oldPath);
            // System.out.println("visitor.getCurrentPath(): " +
            // data.currentPath);
            // System.out.println("visitor.getFullOldPath(): " +
            // data.fullOldPath);
            // System.out.println("visitor.getCurrentFullPath(): " +
            // data.fullCurrentPath);
            // System.out.println("visitor.getType(): " + data.type);

            SnapshotInfo snapshot = new SnapshotInfo(visitor.getProjectName(), data.oldPath, data.currentPath,
                    data.fullOldPath, data.fullCurrentPath, data.type);

            Core.getSpyware().takeSnapshot(snapshot);
            Core.getProjectEventHandler().handle(snapshot);
        }

    }
}
