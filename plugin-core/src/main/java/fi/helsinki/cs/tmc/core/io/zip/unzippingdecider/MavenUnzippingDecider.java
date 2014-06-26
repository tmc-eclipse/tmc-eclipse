package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FileIO;

/**
 * Unzipping decider for Maven projects.
 */
public class MavenUnzippingDecider extends AbstractUnzippingDecider {

    public MavenUnzippingDecider(Project project) {
        super(project);
    }

    /**
     * Prevents unzipping in src/main-folder so that user source files are not
     * overwritten.
     */
    @Override
    public boolean shouldUnzip(String filePath) {
        String s = project.getRootPath() + "/src/main";
        if (filePath.startsWith(s) && (filePath.equals(s) || filePath.charAt(s.length()) == '/')) {
            return !(new FileIO(filePath).fileExists());
        }
        return super.shouldUnzip(filePath);
    }

}
