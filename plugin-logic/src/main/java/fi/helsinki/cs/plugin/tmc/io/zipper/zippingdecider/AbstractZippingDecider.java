package fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider;

// TODO Remove comments when TmcProjectInfo gets ported
public abstract class AbstractZippingDecider implements ZippingDecider {
    // protected TmcProjectInfo projectInfo;

    public AbstractZippingDecider(/* TmcProjectInfo projectInfo */) {
        // this.projectInfo = projectInfo;
    }

    @Override
    public boolean shouldZip(String zipPath) {
        /*
         * File dir = new
         * File(projectInfo.getProjectDirAsFile().getParentFile(), zipPath); if
         * (!dir.isDirectory()) { return true; }
         * 
         * return !new File(dir, ".tmcnosubmit").exists();
         */
        return true; // PLACEHOLDER CODE
    }
}
