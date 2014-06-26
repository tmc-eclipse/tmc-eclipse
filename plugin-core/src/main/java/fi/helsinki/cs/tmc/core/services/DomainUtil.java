package fi.helsinki.cs.tmc.core.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.tmc.core.domain.Course;

/**
 * Helper class that gets course names from given courses.
 */
public class DomainUtil {

    public static String[] getCourseNames(List<Course> courses) {
        List<String> courseNames = new ArrayList<String>();
        for (Course c : courses) {
            courseNames.add(c.getName());
        }
        return courseNames.toArray(new String[0]);
    }

}
