package fi.helsinki.cs.plugin.tmc.domain;

import java.util.List;

public enum ProjectType {

    JAVA_ANT("build.xml"), JAVA_MAVEN("pom.xml"), MAKEFILE("Makefile");

    private final String buildFile;

    private ProjectType(String buildFile) {
        this.buildFile = buildFile;
    }

    public String getBuildFile() {
        return buildFile;
    }

    public static ProjectType findProjectType(List<String> fileList) {
        for (String file : fileList) {
            for (ProjectType type : ProjectType.values()) {
                if (file.contains(type.getBuildFile())) {
                    return type;
                }
            }
        }
        return null;
    }

}
