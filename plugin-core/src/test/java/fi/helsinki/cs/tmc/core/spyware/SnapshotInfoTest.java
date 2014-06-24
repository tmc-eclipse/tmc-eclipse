package fi.helsinki.cs.tmc.core.spyware;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SnapshotInfoTest {
    private SnapshotInfo info;

    @Before
    public void setUp() throws Exception {
        info = new SnapshotInfo("projectName", "oldFilePath", "currentFilePath", "oldFileFullPath", "fullFilePath",
                ChangeType.NONE);
    }

    @Test
    public void constructorSetsValues() {
        assertEquals("projectName", info.getProjectName());
        assertEquals("oldFilePath", info.getOldFilePath());
        assertEquals("currentFilePath", info.getCurrentFilePath());
        assertEquals("oldFileFullPath", info.getOldFullFilePath());
        assertEquals("fullFilePath", info.getCurrentFullFilePath());
        assertEquals(ChangeType.NONE, info.getChangeType());
    }

    @Test
    public void pathsAreEmptyReturnsTrueWhenAllAreNull() {
        info = new SnapshotInfo(null, null, null, null, null, ChangeType.NONE);
        assertTrue(info.pathsAreEmpty());
    }

    @Test
    public void pathsAreEmptyReturnsTrueWhenAllAreEmptyString() {
        info = new SnapshotInfo("", "", "", "", "", ChangeType.NONE);
        assertTrue(info.pathsAreEmpty());
    }

    @Test
    public void pathsAreEmptyReturnsFalseWhenEvenOneIsNotNullOrEmptyString() {
        info = new SnapshotInfo("", "a", "", "", "", ChangeType.NONE);
        assertFalse(info.pathsAreEmpty());

        info = new SnapshotInfo("", "", "a", "", "", ChangeType.NONE);
        assertFalse(info.pathsAreEmpty());

        info = new SnapshotInfo("", "", "", "a", "", ChangeType.NONE);
        assertFalse(info.pathsAreEmpty());

        info = new SnapshotInfo("", "", "", "", "a", ChangeType.NONE);
        assertFalse(info.pathsAreEmpty());
    }

    @Test
    public void toStringReturnsCorrectString() {
        String s = "\tProjectname: pn\n\tOld file path: ofp\n\tCurrent file path: cfp\n\tFull old path: offp\n\tFull current path: cffp\n\tType: "
                + ChangeType.NONE;
        info = new SnapshotInfo("pn", "ofp", "cfp", "offp", "cffp", ChangeType.NONE);
        assertEquals(s, info.toString());
    }

}
