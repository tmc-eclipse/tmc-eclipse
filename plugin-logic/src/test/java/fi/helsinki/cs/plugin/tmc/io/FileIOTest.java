package fi.helsinki.cs.plugin.tmc.io;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FileIOTest {

    private IO io;

    @Before
    public void setUp() {
        io = new FileIO("invalid.file");
    }

    @Test
    public void testGetReaderReturnsNullIfFileDoesntExist() {
        assertNull(io.getReader());
    }

}
