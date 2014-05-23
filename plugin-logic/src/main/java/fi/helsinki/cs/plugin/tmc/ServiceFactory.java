package fi.helsinki.cs.plugin.tmc;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Courses;
import fi.helsinki.cs.plugin.tmc.services.ExerciseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.storage.CourseDAO;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;

public final class ServiceFactory {

    public static final String LOCAL_COURSES_PATH = "courses.tmp";

    private Settings settings;
    private Courses courses;
    private CourseFetcher courseFetcher;
    private ExerciseFetcher exerciseFetcher;
    private ServerManager server;
    private Gson gson;

    public ServiceFactory() {
        this.server = new ServerManager();
        this.settings = Settings.getDefaultSettings();
        IO io = new FileIO(LOCAL_COURSES_PATH);
        CourseDAO courseDAO = new LocalCourseStorage(io);
        this.courses = new Courses(courseDAO);
        this.courseFetcher = new CourseFetcher(courses, server);
        this.exerciseFetcher = new ExerciseFetcher(courses, server);
    }

    public Settings getSettings() {
        return settings;
    }

    public Courses getCourses() {
        return courses;
    }

    public CourseFetcher getCourseFetcher() {
        return courseFetcher;
    }

    public ExerciseFetcher getExerciseFetcher() {
        return exerciseFetcher;
    }

}
