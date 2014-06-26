package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UnzipAllTheThingsTest {

    private UnzipAllTheThings decider;

    @Before
    public void setUp() {
        decider = new UnzipAllTheThings();
    }

    @Test
    public void unzipsEverything() {
        assertTrue(decider.shouldUnzip("/project/StudentFile.txt"));
        assertTrue(decider.shouldUnzip("/project/src/Main.java"));
        assertTrue(decider.shouldUnzip("/project/src/main/Main.java"));
    }

}
