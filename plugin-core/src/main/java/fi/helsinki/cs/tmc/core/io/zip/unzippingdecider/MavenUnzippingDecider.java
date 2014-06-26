package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.IOFactory;

/**
 * Unzipping decider for Maven projects.
 */
public class MavenUnzippingDecider extends AbstractUnzippingDecider {

    public MavenUnzippingDecider(IOFactory io, Project project) {
        super(io, project);
    }

    /**
     * Prevents overwriting files in /src/main folder when unzipping so that
     * changes made by the user will not be lost.
     */
    @Override
    public boolean shouldUnzip(String filePath) {
        String s = project.getRootPath() + "/src/main";
        if (filePath.startsWith(s) && (filePath.equals(s) || filePath.charAt(s.length()) == '/')) {
            return !(io.newFile(filePath).fileExists());
        }
        return super.shouldUnzip(filePath);
    }

}
