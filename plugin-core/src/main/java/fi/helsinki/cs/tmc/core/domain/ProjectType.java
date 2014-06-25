package fi.helsinki.cs.tmc.core.domain;

import java.util.List;

/**
 * An enumeration class for representing the project type.
 */
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
     * and checking if files used by the build tool such as pom.xml or Makefile
     * are present.
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
