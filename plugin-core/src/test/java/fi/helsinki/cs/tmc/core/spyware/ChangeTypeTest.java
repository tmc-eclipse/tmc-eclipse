package fi.helsinki.cs.tmc.core.spyware;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ChangeTypeTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void requiredEnumsExist() {
        assertNotNull(ChangeType.NONE);
        assertNotNull(ChangeType.FILE_CREATE);
        assertNotNull(ChangeType.FILE_RENAME);
        assertNotNull(ChangeType.FILE_CHANGE);
        assertNotNull(ChangeType.FILE_DELETE);
        assertNotNull(ChangeType.FOLDER_CREATE);
        assertNotNull(ChangeType.FOLDER_DELETE);
        assertNotNull(ChangeType.FOLDER_RENAME);
    }

}
