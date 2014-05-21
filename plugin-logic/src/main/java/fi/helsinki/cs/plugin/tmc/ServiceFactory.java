package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Courses;
import fi.helsinki.cs.plugin.tmc.services.ExerciseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.web.WebDao;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;

public class ServiceFactory {

	public static final String LOCAL_COURSES_PATH = "courses.tmp";
	
	private Settings settings;
	private Courses courses;
	private CourseFetcher courseFetcher;
	private ExerciseFetcher exerciseFetcher;
	private WebDao webDAO;
	
	public ServiceFactory() {
		this.webDAO = new WebDao();
		this.settings = Settings.getDefaultSettings();
		this.courses = new Courses(new LocalCourseStorage(new FileIO(LOCAL_COURSES_PATH)));
		this.courseFetcher = new CourseFetcher(courses, webDAO);
		this.exerciseFetcher = new ExerciseFetcher(courses, webDAO);
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
	
	public ExerciseFetcher getExerciseFetcher(){
		return exerciseFetcher;
	}
	
}
