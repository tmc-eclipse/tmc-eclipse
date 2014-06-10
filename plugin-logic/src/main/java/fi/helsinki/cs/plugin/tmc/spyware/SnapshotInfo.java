package fi.helsinki.cs.plugin.tmc.spyware;

public class SnapshotInfo {
    private final String projectName;
    private final String oldFilePath; // empty string unless rename operation,
                                      // then it contains old path
    private final String currentFilePath; // path to file that triggered the
    private final String relativePath; // operation; in case of rename, the
                                          // current name
    private final ChangeType type;

    public SnapshotInfo(String projectName, String oldFilePath, String currentFilePath, String relativePath, ChangeType type) {
        this.projectName = projectName;
        this.oldFilePath = oldFilePath;
        this.currentFilePath = currentFilePath;
        this.type = type;
        this.relativePath = "";
    }

    public String getProjectName() {
        return projectName;
    }

    public String getOldFilePath() {
        return oldFilePath;
    }
    
    public String getRelativePath(){
        return relativePath;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public ChangeType getChangeType() {
        return type;
    }

}
