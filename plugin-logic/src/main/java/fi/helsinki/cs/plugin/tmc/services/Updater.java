package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class Updater {

    private ServerManager server;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;

    public Updater(ServerManager server, CourseDAO courseDAO, ProjectDAO projectDAO) {
        this.server = server;
        this.courseDAO = courseDAO;
        this.projectDAO = projectDAO;
    }

    public void updateCourses() {
        List<Course> oldCourses = courseDAO.getCourses();
        List<Course> newCourses = server.getCourses();

        for (Course newCourse : newCourses) {
            Course oldCourse = findEqualCourse(oldCourses, newCourse);
            if (oldCourse != null) {
                updateCourse(oldCourse, newCourse);
            }
        }

        courseDAO.setCourses(newCourses);
    }

    private void updateCourse(Course oldCourse, Course newCourse) {
        if (oldCourse.getExercises() != null) {
            for (Exercise e : oldCourse.getExercises()) {
                // Update Exercise.course
                e.setCourse(newCourse);
            }
            // Update Course.exercise
            newCourse.setExercises(oldCourse.getExercises());
        }
    }

    private Course findEqualCourse(List<Course> list, Course course) {
        for (Course c : list) {
            if (c.equals(course)) {
                return c;
            }
        }
        return null;
    }

    public void updateExercises(Course course) {
        if (course == null) {
            throw new UserVisibleException("Remember to select your course from TMC -> Settings");
        }

        List<Exercise> oldExercises = course.getExercises();
        List<Exercise> newExercises = server.getExercises(course.getId() + "");

        for (Exercise newExercise : newExercises) {
            Exercise oldExercise = findEqualExercise(oldExercises, newExercise);
            if (oldExercise != null) {
                updateExercise(oldExercise, newExercise);
            }
        }

        course.setExercises(newExercises);
    }

    private void updateExercise(Exercise oldExercise, Exercise newExercise) {
        // Update Exercise.course
        newExercise.setCourse(oldExercise.getCourse());
        newExercise.setProject(oldExercise.getProject());

        // Update Project.exercise
        Project project = projectDAO.getProjectByExercise(oldExercise);
        if (project != null) {
            project.setExercise(newExercise);
        }
    }

    private Exercise findEqualExercise(List<Exercise> list, Exercise exercise) {
        for (Exercise e : list) {
            if (e.equals(exercise)) {
                return e;
            }
        }
        return null;
    }

}
