package fi.helsinki.cs.tmc.core.spyware;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.spyware.DocumentInfo;

public class DocumentInfoTest {
    private DocumentInfo info;

    @Before
    public void setUp() throws Exception {
        info = new DocumentInfo("fullPath", "relativePath", "editorText", "eventText", 1, 2);
    }

    @Test
    public void constructorSetsValues() {
        assertEquals("fullPath", info.getFullPath());
        assertEquals("relativePath", info.getRelativePath());
        assertEquals("editorText", info.getEditorText());
        assertEquals("eventText", info.getEventText());
        assertEquals(1, info.getOffset());
        assertEquals(2, info.getLength());
    }

}
