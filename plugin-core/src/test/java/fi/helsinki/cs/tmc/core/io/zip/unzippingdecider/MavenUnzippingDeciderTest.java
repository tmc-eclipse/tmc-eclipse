package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FakeIOFactory;

public class MavenUnzippingDeciderTest {

    private Project project;
    private FakeIOFactory io;
    private MavenUnzippingDecider decider;

    @Before
    public void setUp() {
        project = mock(Project.class);
        when(project.getRootPath()).thenReturn("/project");

        io = new FakeIOFactory();
        io.getFake("/project").setDirectoryExists();
        io.getFake("/project/src").setDirectoryExists();

        io.getFake("/project/src/Exists.java").setFileExists();
        io.getFake("/project/src/DoesNotExist.java").setDoesNotExist();

        io.getFake("/project/src/main/Exists.java").setFileExists();
        io.getFake("/project/src/main/DoesNotExist.java").setDoesNotExist();

        io.getFake("/project/src/test").setDirectoryExists();
        io.getFake("/project/src/test/Test.java").setFileExists();

        decider = new MavenUnzippingDecider(io, project);
    }

    @Test
    public void testDoesUnzipAndOverwriteToSrc() {
        assertTrue(decider.shouldUnzip("/project/src/DoesNotExist.java"));
        assertTrue(decider.shouldUnzip("/project/src/Exists.java"));
    }

    @Test
    public void testUnzipsToSrcMain() {
        assertTrue(decider.shouldUnzip("/project/src/main/DoesNotExist.java"));
    }

    @Test
    public void testDoesNotOverwriteExistingInSrcMain() {
        assertFalse(decider.shouldUnzip("/project/src/main/Exists.java"));
    }

    @Test
    public void testDoesUnzipTests() {
        assertTrue(decider.shouldUnzip("/project/src/test/Test.java"));
    }

}
