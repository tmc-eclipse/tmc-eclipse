package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.storage.DataSource;

/**
 * Class that handles Course object loading, saving and accessing
 * 
 */
public class CourseDAO {

    private DataSource<Course> dataSource;
    private List<Course> courses;

    public CourseDAO(DataSource<Course> dataSource) {
        this.dataSource = dataSource;
        loadCourses();
    }

    public void loadCourses() {
        this.courses = dataSource.load();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        dataSource.save(courses);
    }

    public void updateCourse(Course course) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equals(course.getName())) {
                courses.set(i, course);
                break;
            }
        }
        dataSource.save(courses);
    }

    public Course getCurrentCourse(Settings settings) {
        return getCourseByName(settings.getCurrentCourseName());
    }

    public Course getCourseByName(String name) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null;
    }

}
