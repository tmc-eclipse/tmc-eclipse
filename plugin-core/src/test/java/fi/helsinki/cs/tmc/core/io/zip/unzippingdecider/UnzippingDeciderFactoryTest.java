package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectType;
import fi.helsinki.cs.tmc.core.io.FakeIOFactory;

public class UnzippingDeciderFactoryTest {

    private UnzippingDeciderFactory factory;

    @Before
    public void setUp() {
        factory = new UnzippingDeciderFactory(new FakeIOFactory());
    }

    @Test
    public void testFactoryGivesRightDeciderForAnt() {
        Project project = mock(Project.class);
        when(project.getProjectType()).thenReturn(ProjectType.JAVA_ANT);
        assertTrue(factory.createUnzippingDecider(project) instanceof DefaultUnzippingDecider);
    }

    @Test
    public void testFactoryGivesRightDeciderForMaven() {
        Project project = mock(Project.class);
        when(project.getProjectType()).thenReturn(ProjectType.JAVA_MAVEN);
        assertTrue(factory.createUnzippingDecider(project) instanceof MavenUnzippingDecider);
    }

    @Test
    public void testFactoryGivesRightDeciderForMakefile() {
        Project project = mock(Project.class);
        when(project.getProjectType()).thenReturn(ProjectType.MAKEFILE);
        assertTrue(factory.createUnzippingDecider(project) instanceof DefaultUnzippingDecider);
    }

    @Test
    public void testFactoryGivesRightDeciderForNone() {
        Project project = mock(Project.class);
        when(project.getProjectType()).thenReturn(ProjectType.NONE);
        assertTrue(factory.createUnzippingDecider(project) instanceof DefaultUnzippingDecider);
    }

    @Test
    public void testFactoryGivesRightDeciderForNullProject() {
        assertTrue(factory.createUnzippingDecider(null) instanceof UnzipAllTheThings);
    }

}
