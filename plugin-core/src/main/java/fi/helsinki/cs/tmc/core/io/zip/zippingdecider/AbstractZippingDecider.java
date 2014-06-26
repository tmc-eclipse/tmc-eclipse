package fi.helsinki.cs.tmc.core.io.zip.zippingdecider;

import java.io.File;

import fi.helsinki.cs.tmc.core.domain.Project;

/**
 * Abstract base class for all zipping deciders.
 */
public abstract class AbstractZippingDecider implements ZippingDecider {
    protected Project project;

    public AbstractZippingDecider(Project project) {
        this.project = project;
    }

    /**
     * Does not include any folders that contain .tmcnosubmit-file
     */
    @Override
    public boolean shouldZip(String zipPath) {

        File dir = new File(new File(project.getRootPath()).getParentFile(), zipPath);
        if (!dir.isDirectory()) {
            return true;
        }

        return !new File(dir, ".tmcnosubmit").exists();

    }
}
