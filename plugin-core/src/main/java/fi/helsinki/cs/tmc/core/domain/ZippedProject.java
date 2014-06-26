package fi.helsinki.cs.tmc.core.domain;

/**
 * Class that stores the project as a zip. Used when downloading exercises from
 * the server; all exercises are initially zipped.
 */
public class ZippedProject {

    private byte[] bytes;

    public ZippedProject() {

    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
