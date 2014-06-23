package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

import fi.helsinki.cs.plugin.tmc.domain.Project;

/**
 * Factory class that returns correct unzipping decider based on project type
 * 
 */
public final class UnzippingDeciderFactory {

    public static UnzippingDecider createUnzippingDecider(Project project) {
        // project is null on initial unzipping as it is not present in project
        // database. Therefore we just unzip all the files
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
