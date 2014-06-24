package fi.helsinki.cs.tmc.core.io.zipper.zippingdecider;

/**
 * Interface for zipping deciders that tell whether the given file or directory
 * should be zipped.
 * 
 */
public interface ZippingDecider {
    /**
     * Zip paths are separated by slashes and don't have a starting slash.
     * Directory paths always end in a slash.
     */
    boolean shouldZip(String zipPath);
}