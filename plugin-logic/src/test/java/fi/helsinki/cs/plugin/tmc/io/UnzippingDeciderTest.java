package fi.helsinki.cs.plugin.tmc.io;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ProjectType;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.DefaultUnzippingDecider;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.MavenUnzippingDecider;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UNZIP_ALL_THE_THINGS;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UnzippingDecider;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UnzippingDeciderFactory;

public class UnzippingDeciderTest {
    private UnzippingDecider decider;
    private Project project;
    private String projectPath;

    @Before
    public void setUp() throws Exception {
        projectPath = FileUtil.getUnixPath(FileUtil.append(new File(".").getCanonicalPath(), "src/test/java/fi/helsinki/cs/plugin/tmc/io/unzippingdecider"));
        project = mock(Project.class);
        when(project.getRootPath()).thenReturn(projectPath);
    }

    @Test
    public void unzipAllTheThings() {
        this.decider = new UNZIP_ALL_THE_THINGS();
        assertTrue(decider.shouldUnzip("/src"));
    }

    @Test
    public void defaultUnzippingDeciderTest() {
        this.decider = new DefaultUnzippingDecider(project);
        assertTrue(decider.shouldUnzip(projectPath + "/lib/Main"));
        assertTrue(decider.shouldUnzip(projectPath + "/src"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/Test"));
        assertTrue(!decider.shouldUnzip(projectPath + "/src/main/Main"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/main/Test"));
        assertTrue(!decider.shouldUnzip(projectPath + "/src/test/Main"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/test/Test"));
    }

    @Test
    public void mavenUnzippingDeciderTest() {
        this.decider = new MavenUnzippingDecider(project);
        assertTrue(decider.shouldUnzip(projectPath + "/lib/Main"));
        assertTrue(decider.shouldUnzip(projectPath + "/src"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/Main"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/Test"));
        assertTrue(!decider.shouldUnzip(projectPath + "/src/main/Main"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/main/Test"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/test/Main"));
        assertTrue(decider.shouldUnzip(projectPath + "/src/test/Test"));
    }

    @Test
    public void abstractUnzippingDeciderTest() throws IOException {
        File file = new File("src/test/java/fi/helsinki/cs/plugin/tmc/io/decider");
        when(project.getRootPath()).thenReturn(file.getCanonicalPath());
        this.decider = new DefaultUnzippingDecider(project);
        assertTrue(!decider.shouldUnzip(file.getCanonicalPath() + "/aaa.java"));
        assertTrue(!decider.shouldUnzip(file.getCanonicalPath() + "/bbb.xml"));
        assertTrue(decider.shouldUnzip(file.getCanonicalPath() + "/asd"));
        assertTrue(decider.shouldUnzip(file.getCanonicalPath() + "/bbb.xmlasds"));
        assertTrue(!decider.shouldUnzip(file.getCanonicalPath() + "/asdasd/"));
    }

    @Test
    public void abstractUnzippingDeciderWhenInvalidProjectPath() {
        when(project.getRootPath()).thenReturn("saddfsafdsad/jhkdshfsa");
        this.decider = new DefaultUnzippingDecider(project);
        assertTrue(decider.shouldUnzip("dsfasdfsadg"));
    }

    @Test
    public void factoryTest() {
        when(project.getProjectType()).thenReturn(ProjectType.JAVA_ANT);
        assertTrue(UnzippingDeciderFactory.createUnzippingDecider(project) instanceof DefaultUnzippingDecider);
        when(project.getProjectType()).thenReturn(ProjectType.JAVA_MAVEN);
        assertTrue(UnzippingDeciderFactory.createUnzippingDecider(project) instanceof MavenUnzippingDecider);
        when(project.getProjectType()).thenReturn(ProjectType.MAKEFILE);
        assertTrue(UnzippingDeciderFactory.createUnzippingDecider(project) instanceof DefaultUnzippingDecider);
        assertTrue(UnzippingDeciderFactory.createUnzippingDecider(null) instanceof UNZIP_ALL_THE_THINGS);
    }

}
