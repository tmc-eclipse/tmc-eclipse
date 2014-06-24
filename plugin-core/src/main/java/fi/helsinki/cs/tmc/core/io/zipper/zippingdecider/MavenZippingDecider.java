package fi.helsinki.cs.tmc.core.io.zipper.zippingdecider;

import java.util.regex.Pattern;

import fi.helsinki.cs.tmc.core.domain.Project;

/**
 * Zipping decider for maven projects
 * 
 */
public class MavenZippingDecider extends AbstractZippingDecider {

    private static final Pattern REJECT_PATTERN = Pattern.compile("^[^/]+/(target|lib/testrunner)/.*");

    public MavenZippingDecider(Project project) {
        super(project);
    }

    /**
     * Prevents zipping any files that matches the REJECT_PATTERN regex.
     */
    @Override
    public boolean shouldZip(String zipPath) {
        if (!super.shouldZip(zipPath)) {
            return false;
        }

        return !REJECT_PATTERN.matcher(zipPath).matches();
    }
}
