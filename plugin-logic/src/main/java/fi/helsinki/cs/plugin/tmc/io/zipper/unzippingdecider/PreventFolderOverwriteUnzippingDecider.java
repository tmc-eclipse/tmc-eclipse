package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

import java.io.File;

public class PreventFolderOverwriteUnzippingDecider implements UnzippingDecider {

    private String[] protectedFolders;

    public PreventFolderOverwriteUnzippingDecider(String... folders) {
        protectedFolders = folders;
    }

    @Override
    public boolean shouldUnzip(String filePath) {

        for (String folder : protectedFolders) {

            String folderPath = File.separator + folder + File.separator;

            if (filePath.contains(folderPath)) {
                File f = new File(filePath);
                if (f.exists()) {
                    return false;
                }
            }
        }
        return true;
    }

}
