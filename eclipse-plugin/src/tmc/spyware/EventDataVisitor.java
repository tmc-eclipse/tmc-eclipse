package tmc.spyware;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;

/**
 * This class handles parsing the resource change event that triggers on file
 * change (save\rename\addition\removal)
 * 
 * Event data is organised as a tree. EventDataVisitor is called by eclipse
 * whenever it visits a tree node. Rename event is delete + add event in tree.
 * Add and delete can be an arbitrary order. Parsing this is annoying
 * */
class EventDataVisitor implements IResourceDeltaVisitor {
    class EventData {

        String oldPath = "";
        String currentPath = "";

        String fullOldPath = "";
        String fullCurrentPath = "";

        String movedFromPath = "";
        String movedToPath = "";

        ChangeType type = ChangeType.NONE;
    }

    private List<EventData> events;

    private boolean done;
    private boolean folderOperation;

    private String projectName;

    public EventDataVisitor() {
        done = false;
        folderOperation = false;
        events = new ArrayList<EventData>();
    }

    public String getProjectName() {
        return projectName;
    }

    public List<EventData> getEvents() {
        return events;
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
            EventData data = new EventData();
            createPaths(data, delta);
            data.type = ChangeType.FILE_CHANGE;
            events.add(data);
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
        if (folderOperation) {
            return;
        }

        if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) {

            handleDeleteRenamePortion(delta, ChangeType.FILE_RENAME);
        } else {

            EventData data = new EventData();
            data.type = ChangeType.FILE_DELETE;
            createPaths(data, delta);
            events.add(data);
        }
    }

    private void folderRemoved(IResourceDelta delta) {
        folderOperation = true;

        if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) {

            handleDeleteRenamePortion(delta, ChangeType.FOLDER_RENAME);
        } else {

            EventData data = new EventData();
            data.type = ChangeType.FOLDER_DELETE;
            createPaths(data, delta);
            events.add(data);
        }
    }

    private void handleDeleteRenamePortion(IResourceDelta delta, ChangeType type) {

        EventData data = findDeleteEventPairForRename(delta, type);

        if (data == null) {
            data = new EventData();
            data.type = type;
            data.movedToPath = delta.getMovedToPath().toString();
            events.add(data);
        }

        createOldPaths(data, delta);
    }

    private EventData findDeleteEventPairForRename(IResourceDelta delta, ChangeType type) {
        EventData data = null;
        // check if this is already in the list
        for (EventData event : events) {
            if (delta.getFullPath().toString().equals(event.movedFromPath) && event.type == type) {
                data = event;
                break;
            }
        }
        return data;
    }

    private void added(IResourceDelta delta) {
        if (delta.getResource() instanceof IFolder) {
            folderAdded(delta);
        } else {
            if (folderOperation) {
                return;
            }
            fileAdded(delta);
        }
    }

    private void fileAdded(IResourceDelta delta) {
        if (folderOperation) {
            return;
        }

        if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
            handleAddRenamePortion(delta, ChangeType.FILE_RENAME);

        } else {
            EventData data = new EventData();

            data.type = ChangeType.FILE_CREATE;
            createPaths(data, delta);
            events.add(data);
        }
    }

    private void folderAdded(IResourceDelta delta) {
        folderOperation = true;
        if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
            handleAddRenamePortion(delta, ChangeType.FOLDER_RENAME);

        } else {
            EventData data = new EventData();

            data.type = ChangeType.FOLDER_CREATE;
            createPaths(data, delta);
            events.add(data);
        }
    }

    private void handleAddRenamePortion(IResourceDelta delta, ChangeType type) {
        EventData data = findAddEventPairForRename(delta, type);
        if (data == null) {
            data = new EventData();
            data.type = type;
            data.movedFromPath = delta.getMovedFromPath().toString();
            events.add(data);
        }
        createPaths(data, delta);
    }

    private EventData findAddEventPairForRename(IResourceDelta delta, ChangeType type) {
        EventData data = null;
        // check if this is already in the list
        for (EventData event : events) {
            if (delta.getFullPath().toString().equals(event.movedToPath) && event.type == type) {
                data = event;
                break;
            }
        }
        return data;
    }

    private void createPaths(EventData data, IResourceDelta delta) {
        if (delta.getResource() != null && delta.getResource().getLocation() != null) {
            data.currentPath = delta.getResource().getFullPath().toString();
            data.fullCurrentPath = delta.getResource().getLocation().toString();
        }
    }

    private void createOldPaths(EventData data, IResourceDelta delta) {
        if (delta.getResource() != null && delta.getResource().getLocation() != null) {
            data.oldPath = delta.getResource().getFullPath().toString();
            data.fullOldPath = delta.getResource().getLocation().toString();
        }
    }
}