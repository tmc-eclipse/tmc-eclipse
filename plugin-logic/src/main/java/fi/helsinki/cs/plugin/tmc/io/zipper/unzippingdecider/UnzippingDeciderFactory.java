package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

public final class UnzippingDeciderFactory {

    public static UnzippingDecider noSrcOverwrite() {
        return new PreventFolderOverwriteUnzippingDecider("src");
    }

    // utility class, should not be constructed
    private UnzippingDeciderFactory() {

    }
}
