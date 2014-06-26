package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FileIO;

/**
 * Unzipping decider for java ant and C projects.
 */
public class DefaultUnzippingDecider extends AbstractUnzippingDecider {

    public DefaultUnzippingDecider(Project project) {
        super(project);
    }

    /**
     * Prevents unzipping in /src folder so that user source files are not
     * overwritten.
     */
    @Override
    public boolean shouldUnzip(String filePath) {
        String s = project.getRootPath() + "/src";
        if (filePath.startsWith(s) && (filePath.equals(s) || filePath.charAt(s.length()) == '/')) {
            return !(new FileIO(filePath).fileExists());
        }
        return super.shouldUnzip(filePath);
    }

}
