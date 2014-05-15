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
    	ProofOfConcept t = new ProofOfConcept();
    	System.out.println(t.tervehdi());
        System.out.println( "Hello World!" );
    }
}
