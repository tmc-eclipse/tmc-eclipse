package fi.helsinki.cs.plugin.tmc.domain;

public class Project {

    private String rootPath;

    public Project() {

    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public boolean containsFile(String file) {
        return false;
    }

}
