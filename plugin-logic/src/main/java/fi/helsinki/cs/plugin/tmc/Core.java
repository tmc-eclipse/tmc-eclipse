package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Courses;
import fi.helsinki.cs.plugin.tmc.services.ExerciseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Settings;

public final class Core {

    private static Core core;

    private TMCErrorHandler errorHandler;
    private Settings settings;
    private CourseFetcher courseFetcher;
    private ExerciseFetcher exerciseFetcher;
    private Courses courses;

    private Core() {
        ServiceFactory factory = new ServiceFactory();
        this.settings = factory.getSettings();
        this.courseFetcher = factory.getCourseFetcher();
        this.exerciseFetcher = factory.getExerciseFetcher();
        this.courses = factory.getCourses();
    }

    public static void setTMCErrorHandler(TMCErrorHandler errorHandler) {
        Core.getInstance().errorHandler = errorHandler;
    }

    public static TMCErrorHandler getErrorHandler() {
        return Core.getInstance().errorHandler;
    }

    public static Settings getSettings() {
        return Core.getInstance().settings;
    }

    public static CourseFetcher getCourseFetcher() {
        return Core.getInstance().courseFetcher;
    }

    public static ExerciseFetcher getExerciseFetcher() {
        return Core.getInstance().exerciseFetcher;
    }

    public static Courses getCourses() {
        return Core.getInstance().courses;
    }

    public static Core getInstance() {
        if (core == null) {
            core = new Core();
        }
        return core;
    }

}
