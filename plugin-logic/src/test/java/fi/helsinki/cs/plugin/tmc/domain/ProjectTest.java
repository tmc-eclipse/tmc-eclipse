package fi.helsinki.cs.plugin.tmc.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
	private Project project;
	private Exercise exercise;
	private List<String> projectFiles;

	@Before
	public void setUp() throws Exception {
		projectFiles = new ArrayList<String>();
		
		this.project = new Project(null, projectFiles);
		this.exercise = new Exercise("testName", "testCourse");
	}
	
	@Test
	public void testHashCode(){
		assertEquals(null, project.getExercise());
		assertEquals(0, project.hashCode());
		project.setExercise(exercise);
		assertTrue(project.hashCode() != 0);
	}
	
	
	
	@Test
	public void isAntProject(){
		projectFiles.add("asdasd.xml");
		projectFiles.add("ssdsaddMakefilesadas");
		projectFiles.add("build.xml");
		
		assertEquals(ProjectType.JAVA_ANT, project.getProjectType());
	}
	
	@Test
	public void isMavenProject(){
		projectFiles.add("asdasd.xml");
		projectFiles.add("build.xmlxcvx");
		projectFiles.add("pom.xml");
		
		assertEquals(ProjectType.JAVA_MAVEN, project.getProjectType());
	}
	
	@Test
	public void isCProject(){
		projectFiles.add("Makefile");
		projectFiles.add("asdasd.xml");
		projectFiles.add("pomm.xml");
		
		assertEquals(ProjectType.MAKEFILE, project.getProjectType());
	}
	
	@Test 
	public void IsNotAProject(){
		projectFiles.add("asdasd.xml");
		projectFiles.add("pomm.xml");
		projectFiles.add("build.txt");
		
		assertEquals(null, project.getProjectType());
	}
	
	@Test
	public void testEquals(){
		Project p = new Project(exercise, projectFiles);
		assertTrue(!p.equals(project));
		assertTrue(!project.equals(p));
		
		project.setExercise(exercise);
		assertTrue(p.equals(project));
		
		p.setExercise(new Exercise());
		assertTrue(!p.equals(project));
		
		assertTrue(!project.equals("asdasd"));
		assertTrue(!project.equals(null));
	}
	
	@Test
	public void testContainsFile(){
		projectFiles.add("directory/pom.xml");
		Project p = new Project(exercise, projectFiles);
		
		assertTrue(!p.containsFile(null));
		assertTrue(p.containsFile("directory/trololo"));
	}
	
	@Test
	public void testRootPath(){
		projectFiles.add("directory/pom.xml");
		Project p = new Project(exercise, projectFiles);
		assertEquals("directory", p.getRootPath());
		
		p = new Project(exercise, new ArrayList<String>());
		assertEquals("", p.getRootPath());
	}
	

}
