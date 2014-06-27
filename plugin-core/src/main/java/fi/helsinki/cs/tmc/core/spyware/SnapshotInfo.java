package fi.helsinki.cs.tmc.core.spyware;

/**
 * Helper class that contains necessary data for core to take a snapshot. IDE
 * must fill in this information.
 */
public class SnapshotInfo {
    private final String projectName;
    private final String oldFilePath; // empty string unless rename operation,
                                      // then it contains old path
    private final String currentFilePath; // path to file that triggered the

    private final String oldFullFilePath;
    private final String currentFullFilePath; // operation; in case of rename,
                                              // the
    // current name
    private final ChangeType type;

    public SnapshotInfo(String projectName, String oldFilePath, String currentFilePath, String oldFullFilePath,
            String fullFilePath, ChangeType type) {
        this.projectName = projectName;
        this.oldFilePath = oldFilePath;
        this.currentFilePath = currentFilePath;
        this.type = type;
        this.oldFullFilePath = oldFullFilePath;
        this.currentFullFilePath = fullFilePath;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getOldFilePath() {
        return oldFilePath;
    }

    public String getCurrentFullFilePath() {
        return currentFullFilePath;
    }

    public String getOldFullFilePath() {
        return oldFullFilePath;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public ChangeType getChangeType() {
        return type;
    }

    public boolean pathsAreEmpty() {
        return isEmpty(oldFilePath) && isEmpty(oldFullFilePath) && isEmpty(currentFilePath)
                && isEmpty(currentFullFilePath);
    }

    private boolean isEmpty(String path) {
        return path == null || path.isEmpty();
    }

    @Override
    public String toString() {
        return "\tProjectname: " + projectName + "\n\tOld file path: " + oldFilePath + "\n\tCurrent file path: "
                + currentFilePath + "\n\tFull old path: " + oldFullFilePath + "\n\tFull current path: "
                + currentFullFilePath + "\n\tType: " + type;
    }
}
