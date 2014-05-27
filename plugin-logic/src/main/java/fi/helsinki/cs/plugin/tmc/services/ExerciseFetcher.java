package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class ExerciseFetcher {

    private ServerManager server;
    private CourseDAO courseDAO;
    private Course course;

    public ExerciseFetcher(ServerManager server, CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
        this.server = server;
    }

    public void updateExercisesForCurrentCourse() {
        try {
            this.course = courseDAO.getCourseByName(Core.getSettings().getCurrentCourseName());
            List<Exercise> exercises = server.getExercises("" + course.getId());
            if (exercises != null) {
                course.setExercises(exercises);
            }
        } catch (UserVisibleException e) {
            Core.getErrorHandler().handleException(e);
        }
    }

    public Exercise getExerciseByName(String name) {
        for (Exercise e : course.getExercises()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public List<Exercise> getExercisesForCurrentCourse() {
        return course.getExercises();
    }

    public String[] getCurrentCoursesExerciseNames() {
        List<Exercise> exerciseNames = course.getExercises();
        String[] names = new String[exerciseNames.size()];

        for (int i = 0; i < exerciseNames.size(); i++) {
            names[i] = exerciseNames.get(i).getName();
        }

        return names;
    }
}
