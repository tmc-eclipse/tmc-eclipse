package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public final class UnzippingDeciderFactory {

    public static UnzippingDecider createUnzippingDecider(Project project) {
        if (project == null) {
            return new UNZIP_ALL_THE_THINGS();
        }

        switch (project.getProjectType()) {
        case JAVA_ANT:
            return new DefaultUnzippingDecider(project);
        case JAVA_MAVEN:
            return new MavenUnzippingDecider(project);
        case MAKEFILE:
            return new DefaultUnzippingDecider(project);
        default:
            return new DefaultUnzippingDecider(project);
        }
    }

    // utility class, should not be constructed
    private UnzippingDeciderFactory() {

    }
}
