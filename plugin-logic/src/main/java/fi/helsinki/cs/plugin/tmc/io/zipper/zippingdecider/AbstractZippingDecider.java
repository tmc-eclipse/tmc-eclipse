package fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider;

import java.io.File;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public abstract class AbstractZippingDecider implements ZippingDecider {
    protected Project project;

    public AbstractZippingDecider(Project project) {
        this.project = project;
    }

    @Override
    public boolean shouldZip(String zipPath) {

        File dir = new File(new File(project.getRootPath()).getParentFile(), zipPath);
        if (!dir.isDirectory()) {
            return true;
        }

        return !new File(dir, ".tmcnosubmit").exists();

    }
}
