package fi.helsinki.cs.plugin.tmc.domain;

import java.util.List;

public enum ProjectType {

    JAVA_ANT("build.xml"), JAVA_MAVEN("pom.xml"), MAKEFILE("Makefile"), NONE("\0");

    private final String buildFile;

    private ProjectType(String buildFile) {
        this.buildFile = buildFile;
    }

    public String getBuildFile() {
        return buildFile;
    }

    /**
     * A method that determines the project type by looking at the project files
     * and checking if files such as pom.xml or makefile are present.
     * 
     * @param fileList
     *            list of project files
     * @return project type or none if it could not be determined
     */
    public static ProjectType findProjectType(List<String> fileList) {
        for (String file : fileList) {
            for (ProjectType type : ProjectType.values()) {
                if (file.toLowerCase().endsWith(((type.getBuildFile().toLowerCase())))) {
                    return type;
                }
            }
        }
        return NONE;
    }

}
