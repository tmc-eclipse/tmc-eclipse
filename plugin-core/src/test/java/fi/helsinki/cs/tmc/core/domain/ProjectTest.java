package fi.helsinki.cs.tmc.core.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectType;
import fi.helsinki.cs.tmc.core.io.zipper.zippingdecider.DefaultZippingDecider;
import fi.helsinki.cs.tmc.core.io.zipper.zippingdecider.MavenZippingDecider;

public class ProjectTest {
    private Project project;
    private Exercise exercise;
    private List<String> projectFiles;

    @Before
    public void setUp() throws Exception {
        projectFiles = new ArrayList<String>();

        this.exercise = new Exercise("testName", "testCourse");
        this.project = new Project(exercise, projectFiles);
    }

    @Test
    public void testHashCode() {

        project.setExercise(exercise);
        assertEquals(exercise.hashCode(), project.hashCode());
    }

    @Test
    public void isAntProject() {
        projectFiles.add("asdasd.xml");
        projectFiles.add("ssdsaddMakefilesadas");
        projectFiles.add("build.xml");

        assertEquals(ProjectType.JAVA_ANT, project.getProjectType());
    }

    @Test
    public void isMavenProject() {
        projectFiles.add("asdasd.xml");
        projectFiles.add("build.xmlxcvx");
        projectFiles.add("pom.xml");

        assertEquals(ProjectType.JAVA_MAVEN, project.getProjectType());
    }

    @Test
    public void isCProject() {
        projectFiles.add("Makefile");
        projectFiles.add("asdasd.xml");
        projectFiles.add("pomm.xml");

        assertEquals(ProjectType.MAKEFILE, project.getProjectType());
    }

    @Test
    public void IsNotAProject() {
        projectFiles.add("asdasd.xml");
        projectFiles.add("pomm.xml");
        projectFiles.add("build.txt");

        assertEquals(ProjectType.NONE, project.getProjectType());
    }

    @Test
    public void testEquals() {

        Exercise exercise2 = new Exercise("testname2", "testcourse");
        Project p = new Project(exercise2, projectFiles);

        assertFalse(p.equals(project));
        assertFalse(project.equals(p));

        p.setExercise(exercise);
        assertTrue(p.equals(project));

        p.setExercise(new Exercise());
        assertFalse(p.equals(project));

        assertFalse(project.equals("asdasd"));
        assertFalse(project.equals(null));
    }

    @Test
    public void testContainsFile() {
        projectFiles.add("directory/pom.xml");
        Project p = new Project(exercise, projectFiles);

        assertTrue(!p.containsFile(null));
        assertTrue(p.containsFile("directory/trololo"));
    }

    @Test
    public void testRootPath() {
        projectFiles.add("directory/pom.xml");
        Project p = new Project(exercise, projectFiles);
        assertEquals("directory", p.getRootPath());

        p = new Project(exercise, new ArrayList<String>());
        assertEquals("", p.getRootPath());
    }

    @Test
    public void getZippingDeciderWhenMavenProjectTest() {
        projectFiles.add("pom.xml");
        assertEquals(project.getZippingDecider().getClass(), new MavenZippingDecider(project).getClass());
    }

    @Test
    public void getZippingDeciderWhenAntProjectTest() {
        projectFiles.add("build.xml");
        assertEquals(project.getZippingDecider().getClass(), new DefaultZippingDecider(project).getClass());
    }

    @Test
    public void getZippingDeciderWhenCProjectTest() {
        projectFiles.add("Makefile");
        assertEquals(project.getZippingDecider().getClass(), new DefaultZippingDecider(project).getClass());
    }

    @Test(expected = RuntimeException.class)
    public void getZippingDeciderWhenThereIsNoProjectTest() {
        project.setProjectFiles(new ArrayList<String>());
        project.getZippingDecider();
    }

    @Test
    public void existsOnDiskTest() {
        projectFiles.add("pom.xml");
        assertFalse(project.existsOnDisk());
        projectFiles.add("/src/pom.xml");
        assertTrue(project.existsOnDisk());
        project.setProjectFiles(new ArrayList<String>());
        assertFalse(project.existsOnDisk());
    }

    @Test
    public void constructorTest() {
        project = new Project(new Exercise("name"));
        assertEquals(project.getExtraStudentFiles().size(), 0);
        assertEquals(project.getExercise().getName(), "name");
        assertEquals(project.getReadOnlyProjectFiles().size(), 0);
    }

}
