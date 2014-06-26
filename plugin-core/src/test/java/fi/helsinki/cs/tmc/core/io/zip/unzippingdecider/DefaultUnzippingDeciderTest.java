package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FakeIOFactory;

public class DefaultUnzippingDeciderTest {

    private Project project;
    private FakeIOFactory io;
    private DefaultUnzippingDecider decider;

    @Before
    public void setUp() {
        project = mock(Project.class);
        when(project.getRootPath()).thenReturn("/project");

        io = new FakeIOFactory();
        io.getFake("/project").setDirectoryExists();
        io.getFake("/project/src").setDirectoryExists();

        io.getFake("/project/src/Exists.java").setFileExists();
        io.getFake("/project/src/DoesNotExist.java").setDoesNotExist();

        io.getFake("/project/test").setDirectoryExists();
        io.getFake("/project/test/Test.java").setFileExists();

        decider = new DefaultUnzippingDecider(io, project);
    }

    @Test
    public void testDoesUnzipToSrc() {
        assertTrue(decider.shouldUnzip("/project/src/DoesNotExist.java"));
    }

    @Test
    public void testDoesNotOverwriteExistingInSrc() {
        assertFalse(decider.shouldUnzip("/project/src/Exists.java"));
    }

    @Test
    public void testDoesUnzipTests() {
        assertTrue(decider.shouldUnzip("/project/test/Test.java"));
    }

}
