package fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public class DefaultZippingDecider extends AbstractZippingDecider {

    public DefaultZippingDecider(Project project) {
        super(project);
    }

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