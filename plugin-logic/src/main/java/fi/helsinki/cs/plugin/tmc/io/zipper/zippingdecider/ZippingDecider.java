package fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider;

public interface ZippingDecider {
    /**
     * Tells whether the given file or directory should be zipped.
     * 
     * Zip paths are separated by slashes and don't have a starting slash.
     * Directory paths always end in a slash.
     */
    boolean shouldZip(String zipPath);
}