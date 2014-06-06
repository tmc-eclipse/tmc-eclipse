package tmc.spyware;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;

/*
 * Event data is organized as a tree. EventDataVisitor is called by eclipse whenever it visits a tree node
 * */
class EventDataVisitor implements IResourceDeltaVisitor {
    private String projectName = "";

    private String oldFilePath = ""; // used in case of renaming
    private String currentFilePath = "";

    private ChangeType type = ChangeType.NONE;
    private boolean isBuildEvent = false;

    public String getProjectName() {
        return projectName;
    }

    public String getOldFilePath() {
        return oldFilePath;
    }

    public String getFilePath() {
        return currentFilePath;
    }

    public ChangeType getType() {
        return type;
    }

    public boolean isBuildEvent() {
        return isBuildEvent;
    }

    public boolean visit(IResourceDelta delta) {

        // if this is not a leaf, do nothing
        if (delta.getAffectedChildren().length != 0) {
            return true;
        }

        IResource res = delta.getResource();

        getChangeType(delta);

        if (isCompiledFile(res.getFileExtension())) {
            isBuildEvent = true;
        }

        if (res.getFullPath().segmentCount() > 1 && projectName.equals("")) {
            projectName = res.getFullPath().segment(0);
        }

        if (currentFilePath.equals("")) {
            currentFilePath = res.getFullPath().toString();
        }

        return true; // visit the children; ultimately we care about the leaf
                     // node that caused the change
    }

    private void getChangeType(IResourceDelta delta) {
        boolean isFolder = delta.getResource() instanceof IFolder;

        switch (delta.getKind()) {
        case IResourceDelta.ADDED:
            handleAddition(isFolder, delta.getResource());
            break;
        case IResourceDelta.REMOVED:
            handleRemove(isFolder, delta.getResource());
            break;
        case IResourceDelta.CHANGED:
            if (isFolder) {
                type = ChangeType.FOLDER_CHANGE;
            } else {
                type = ChangeType.FILE_CHANGE;
            }
            break;
        }
    }

    private void handleAddition(boolean isFolder, IResource resource) {

        // delete + add event is rename
        if (type == ChangeType.FOLDER_DELETE || type == ChangeType.FILE_DELETE) {
            oldFilePath = currentFilePath;
            currentFilePath = resource.getFullPath().toString();

            handleRename(isFolder);
            return;
        }

        if (isFolder) {
            type = ChangeType.FOLDER_CREATE;
        } else {
            type = ChangeType.FILE_CREATE;
        }
    }

    private void handleRemove(boolean isFolder, IResource resource) {

        // delete + add event is rename
        if (type == ChangeType.FOLDER_CREATE || type == ChangeType.FILE_CREATE) {

            oldFilePath = resource.getFullPath().toString();
            handleRename(isFolder);
            return;
        }

        if (isFolder) {
            type = ChangeType.FOLDER_DELETE;
        } else {
            type = ChangeType.FILE_DELETE;
        }
    }

    private void handleRename(boolean isFolder) {
        if (isFolder) {
            type = ChangeType.FOLDER_RENAME;
        } else {
            type = ChangeType.FILE_RENAME;
        }
    }

    private boolean isCompiledFile(String extension) {
        if (extension == null) {
            return false;
        }

        if (extension.contains("class")) {
            return true;
        }

        return false;
    }
}