package fi.helsinki.cs.tmc.core.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.ZippedProject;

public class ZippedProjectTest {

    private ZippedProject zp;

    @Before
    public void setup() {
        zp = new ZippedProject();
    }

    @Test
    public void canSetAndGetBytes() {
        byte[] bytes = {1, 0, 1};
        zp.setBytes(bytes);
        assertEquals(bytes, zp.getBytes());
    }
}
