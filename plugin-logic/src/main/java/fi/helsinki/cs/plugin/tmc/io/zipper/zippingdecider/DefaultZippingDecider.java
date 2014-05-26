package fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider;

public class DefaultZippingDecider extends AbstractZippingDecider {

    /*public DefaultZippingDecider(TmcProjectInfo projectInfo) {
        super(projectInfo);
    }*/

    @Override
    public boolean shouldZip(String zipPath) {
    /*    if (!super.shouldZip(zipPath)) {
            return false;
        }

        if (projectInfo.getTmcProjectFile().getExtraStudentFiles().contains(withoutRootDir(zipPath))) {
            return true;
        } else {
            return zipPath.contains("/src/");
        }*/
	
		return true; // PLACEHOLDER CODE
    }

   /* private String withoutRootDir(String zipPath) {
        int i = zipPath.indexOf('/');
        if (i != -1) {
            return zipPath.substring(i + 1);
        } else {
            return "";
        }
    }*/
}