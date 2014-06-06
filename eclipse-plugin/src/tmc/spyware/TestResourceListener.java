package tmc.spyware;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.spyware.SnapshotInfo;

public class TestResourceListener implements IResourceChangeListener {

    @Override
    public void resourceChanged(IResourceChangeEvent event) {

        if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
            return;
        }

        EventDataVisitor p = new EventDataVisitor();
        try {
            event.getDelta().accept(p);
        } catch (CoreException e) {
            return;
        }
        // System.out.println("---------------------------------------------------------------");
        if (!p.isBuildEvent()) { // lots of empty change events on save & build

            Core.getSpyware().takeSnapshot(
                    new SnapshotInfo(p.getProjectName(), p.getOldFilePath(), p.getFilePath(), p.getType()));
        }/*
          * else { System.out.println("Build event - ignoring"); }
          * System.out.println("#########################################");
          */

    }
}
