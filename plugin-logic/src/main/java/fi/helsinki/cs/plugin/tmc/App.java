package fi.helsinki.cs.plugin.tmc;

import java.io.File;
import java.util.ArrayList;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.services.Courses;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//    	ProofOfConcept t = new ProofOfConcept();
//    	System.out.println(t.tervehdi());
//        System.out.println( "Hello World!" );
    	LocalCourseStorage lcs=new LocalCourseStorage(new File("testi.txt"));
    	Courses c=new Courses(lcs);
    	System.out.println(c);
    	Course c1=new Course("c1");
    	Course c2=new Course("c2");
    	Course c3=new Course("c3");
    	Course c4=new Course("c4");
    	ArrayList<Course> cl=new ArrayList<>();
    	
    	cl.add(c1);
    	cl.add(c2);
    	cl.add(c3);
    	cl.add(c4);
    	
    	c.setCourses(cl);
    	
    	System.out.println(c.getCourses()); 
    	
    	System.out.println(c.getCourseByName("c4").getCometUrl());
    	
    	c4.setCometUrl("asd");
    	
    	System.out.println(c.getCourseByName("c4").getCometUrl());
    	
    	System.out.println(lcs.load().get(3).getCometUrl());
    	
    	Course c5 =new Course("c4"); 
    	
    	System.out.println(c.getCourseByName("c4").getCometUrl());
    	
    	System.out.println(lcs.load().get(3).getCometUrl());
    	
//    	lcs.save(cl);
    	
//    	System.out.println(lcs.load().get(3).getCometUrl());
    	
    	c.updateCourse(c5);
    	
    	System.out.println(c.getCourses().get(3)==c4);
    	
    	System.out.println(lcs.load().get(3).getCometUrl());
    	
    	System.out.println(c.getCourseByName("c4").getCometUrl());
    }
}
