package fi.helsinki.cs.plugin.tmc.domain;

import java.util.List;

public enum ProjectType {

    JAVA_ANT("ANT", "build.xml"), JAVA_MAVEN("MAVEN", "pom.xml"), MAKEFILE("C", "Makefile"), NONE("", "\0");

    private final String buildFile;

    private ProjectType(String typeName, String buildFile) {
        this.buildFile = buildFile;
    }

    public String getBuildFile() {
        return buildFile;
    }

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
