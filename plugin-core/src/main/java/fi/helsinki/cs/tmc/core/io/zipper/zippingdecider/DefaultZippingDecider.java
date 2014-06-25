package fi.helsinki.cs.tmc.core.io.zipper.zippingdecider;

import fi.helsinki.cs.tmc.core.domain.Project;

/**
 * Default zipping decider that is used by Java Ant and C projects.
 */
public class DefaultZippingDecider extends AbstractZippingDecider {

    public DefaultZippingDecider(Project project) {
        super(project);
    }

    /**
     * zips extra student files and content of the src folder
     */
    @Override
    public boolean shouldZip(String zipPath) {

        if (!super.shouldZip(zipPath)) {
            return false;
        }

        if (project.getExtraStudentFiles() != null && project.getExtraStudentFiles().contains(withoutRootDir(zipPath))) {
            return true;
        } else {
            return zipPath.contains("/src/");
        }

    }

    private String withoutRootDir(String zipPath) {
        int i = zipPath.indexOf('/');
        if (i != -1) {
            return zipPath.substring(i + 1);
        } else {
            return "";
        }
    }

}