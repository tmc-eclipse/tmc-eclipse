package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.services.web.WebDao;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class CourseFetcher {

    private Courses courses;
    private WebDao webDao;

    public CourseFetcher(Courses courses, WebDao webDao) {
        this.courses = courses;
        this.webDao = webDao;
    }

    public void updateCourses() {
        try {
            courses.setCourses(webDao.getCourses());
        } catch (UserVisibleException e) {
            Core.getErrorHandler().handleException(e);
        }
    }

    public String[] getCourseNames() {
        List<Course> courseList = courses.getCourses();
        String[] names = new String[courseList.size()];

        for (int i = 0; i < courseList.size(); i++) {
            names[i] = courseList.get(i).getName();
        }

        return names;
    }

}
