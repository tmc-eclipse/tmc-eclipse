package fi.helsinki.cs.tmc.core.storage.formats;

import java.util.List;

import fi.helsinki.cs.tmc.core.domain.Course;

public class CoursesFileFormat {

    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}
