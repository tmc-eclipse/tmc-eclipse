package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

/**
 * Unzips all the things
 * 
 */
public class UNZIP_ALL_THE_THINGS implements UnzippingDecider {
    @Override
    public boolean shouldUnzip(String filePath) {
        return true;
    }
}
