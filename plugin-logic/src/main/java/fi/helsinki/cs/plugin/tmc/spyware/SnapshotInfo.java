package fi.helsinki.cs.plugin.tmc.spyware;

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
        return currentFullFilePath;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public ChangeType getChangeType() {
        return type;
    }

}
