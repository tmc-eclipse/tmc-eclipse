package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FakeFileIO;
import fi.helsinki.cs.tmc.core.io.FakeIOFactory;
import fi.helsinki.cs.tmc.core.io.IOFactory;

public class AbstractUnzippingDeciderTest {

    class AbstractUnzippingDeciderImpl extends AbstractUnzippingDecider {

        public AbstractUnzippingDeciderImpl(IOFactory io, Project project) {
            super(io, project);
        }

    }

    private Project project;
    private FakeIOFactory io;
    private AbstractUnzippingDeciderImpl decider;

    @Before
    public void setUp() {
        project = mock(Project.class);
        when(project.getRootPath()).thenReturn("/project");

        io = new FakeIOFactory();
        io.getFake("/project").setDirectoryExists();
        io.getFake("/project/StudentFile.txt").setFileExists();
        io.getFake("/project/NotAStudentFile.txt").setFileExists();

        FakeFileIO tmcProjectFile = io.getFake("/project/.tmcproject.yml");
        tmcProjectFile.setContents("extra_student_files:\n - \"StudentFile.txt\"\n - \"AnotherStudentFile.txt\"");

        decider = new AbstractUnzippingDeciderImpl(io, project);
    }

    @Test
    public void doesOverwriteANonStudentFile() {
        assertTrue(decider.shouldUnzip("/project/NotAStudentFile.txt"));
    }

    @Test
    public void doesNotOverwriteStudentFiles() {
        assertFalse(decider.shouldUnzip("/project/StudentFile.txt"));
    }

}
