package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.IOFactory;

/**
 * Factory class that returns correct unzipping decider based on project type.
 */
public final class UnzippingDeciderFactory {

    private IOFactory io;

    public UnzippingDeciderFactory(IOFactory io) {
        this.io = io;
    }

    public UnzippingDecider createUnzippingDecider(Project project) {
        // project is null on initial unzipping as it is not present in project
        // database. Therefore we just unzip all the files
        if (project == null) {
            return new UnzipAllTheThings();
        }

        switch (project.getProjectType()) {
        case JAVA_ANT:
            return new DefaultUnzippingDecider(io, project);
        case JAVA_MAVEN:
            return new MavenUnzippingDecider(io, project);
        case MAKEFILE:
            return new DefaultUnzippingDecider(io, project);
        default:
            return new DefaultUnzippingDecider(io, project);
        }
    }

}
