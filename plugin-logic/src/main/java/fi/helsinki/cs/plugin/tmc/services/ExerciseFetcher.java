package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class ExerciseFetcher {

    private Settings settings;

    private ServerManager server;
    private CourseDAO courseDAO;
    private Course course;

    public ExerciseFetcher(ServerManager server, CourseDAO courseDAO, Settings settings) {
        this.courseDAO = courseDAO;
        this.server = server;
        this.settings = settings;
    }

    public void updateExercisesForCurrentCourse() {
        try {
            this.course = courseDAO.getCourseByName(settings.getCurrentCourseName());
            List<Exercise> newExercises;
            if (this.course != null) {
                newExercises = server.getExercises("" + course.getId());
            } else {
                newExercises = new ArrayList<Exercise>();
            }
            if (newExercises != null) {
                course.setExercises(newExercises);
            }
        } catch (UserVisibleException e) {
            Core.getErrorHandler().handleException(e);
        } catch (NullPointerException n) {
            Core.getErrorHandler().handleException(
                    new NullPointerException("Remember to select your course from TMC -> Settings"));
        }
    }

    public Exercise getExerciseByName(String name) {
        Course course = courseDAO.getCourseByName(settings.getCurrentCourseName());
        for (Exercise e : course.getExercises()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public List<Exercise> getExercisesForCurrentCourse() {
        if (course != null) {
            return course.getExercises();
        } else {
            return new ArrayList<Exercise>();
        }
    }

    public List<Exercise> getDownloadableExercises() {
        List<Exercise> downloadableExercises = new ArrayList<Exercise>();
        for (Exercise e : getExercisesForCurrentCourse()) {
            System.out.println(e.getName() + ", downloaded: " + e.isDownloaded());
            if (!e.isDownloaded()) {
                downloadableExercises.add(e);
            }
        }
        return downloadableExercises;
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
