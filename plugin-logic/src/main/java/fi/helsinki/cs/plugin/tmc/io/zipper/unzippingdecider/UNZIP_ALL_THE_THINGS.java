package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

public class UNZIP_ALL_THE_THINGS implements UnzippingDecider {
    @Override
    public boolean shouldUnzip(String filePath) {
        return true;
    }
}
