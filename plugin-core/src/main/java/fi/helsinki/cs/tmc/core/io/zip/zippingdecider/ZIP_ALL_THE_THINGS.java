package fi.helsinki.cs.tmc.core.io.zip.zippingdecider;

/**
 * Zips everything.
 */
public class ZIP_ALL_THE_THINGS implements ZippingDecider {

    @Override
    public boolean shouldZip(String zipPath) {
        return true;
    }
}