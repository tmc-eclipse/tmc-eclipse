package tmc.spyware;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;

/*
 * Event data is organized as a tree. EventDataVisitor is called by eclipse whenever it visits a tree node
 * */
class EventDataVisitor implements IResourceDeltaVisitor {
    private boolean done;
    private boolean renamingFolder;

    private String projectName;

    private ChangeType type;

    private String oldPath;
    private String currentPath;

    public EventDataVisitor() {
        this.done = false;
        this.renamingFolder = false;
        this.type = ChangeType.NONE;
        this.oldPath = "";
        this.currentPath = "";
        this.projectName = "";
    }

    public String getProjectName() {
        return projectName;
    }

    public ChangeType getType() {
        return type;
    }

    public boolean visit(IResourceDelta delta) {

        projectName = delta.getResource().getFullPath().segment(0);

        switch (delta.getKind()) {
        case IResourceDelta.ADDED:
            added(delta);
            break;
        case IResourceDelta.REMOVED:
            removed(delta);
            break;
        case IResourceDelta.CHANGED:
            changed(delta);
            break;
        }

        return !done;
    }

    private void changed(IResourceDelta delta) {
        if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
            type = ChangeType.FILE_CHANGE;
        }
    }

    private void removed(IResourceDelta delta) {
        if (delta.getResource() instanceof IFolder) {
            folderRemoved(delta);
        } else {
            fileRemoved(delta);
        }
    }

    private void fileRemoved(IResourceDelta delta) {
        if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) {
            oldPath = delta.getResource().getLocation().toString();
        } else {
            type = ChangeType.FILE_DELETE;
        }
    }

    private void folderRemoved(IResourceDelta delta) {
        if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) {
            type = ChangeType.FOLDER_RENAME;
            oldPath = delta.getResource().getLocation().toString();
            folderRenamingHandler();
        } else {
            type = ChangeType.FOLDER_DELETE;
            done = true;
        }
    }

    private void folderRenamingHandler() {
        if (!renamingFolder) {
            renamingFolder = true;
        } else {
            done = true;
        }
    }

    private void added(IResourceDelta delta) {
        if (delta.getResource() instanceof IFolder) {
            folderAdded(delta);
        } else {
            if (renamingFolder) {
                return;
            }
            fileAdded(delta);
        }
    }

    private void fileAdded(IResourceDelta delta) {
        if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
            currentPath = delta.getResource().getLocation().toString();
            type = ChangeType.FILE_RENAME;
        } else {
            type = ChangeType.FILE_CREATE;
        }
    }

    private void folderAdded(IResourceDelta delta) {
        if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
            type = ChangeType.FOLDER_RENAME;
            currentPath = delta.getResource().getLocation().toString();
            folderRenamingHandler();
        } else {
            type = ChangeType.FOLDER_CREATE;
        }
    }

    public String getOldPath() {
        return oldPath;
    }

    public String getCurrentPath() {
        return currentPath;
    }
}