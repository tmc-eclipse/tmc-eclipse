package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;

public class DomainUtil {

    public static String[] getCourseNames(CourseDAO dao) {
        List<String> courseNames = new ArrayList<String>();
        for (Course c : dao.getCourses()) {
            courseNames.add(c.getName());
        }
        return courseNames.toArray(new String[0]);
    }

}
