package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Courses;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.web.UserVisibleException;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;

public class Core {

	private Settings settings;
	private CourseFetcher courseFetcher;

	public Core() {
		settings = Settings.getDefaultSettings();
		try {
			courseFetcher = new CourseFetcher(new Courses(new LocalCourseStorage(new FileIO("courses.tmp"))));
		} catch (UserVisibleException e) {
			System.out.println("Virhe");
		}
	}
	
	public Settings getSettings(){
		return settings;
	}
	
	public CourseFetcher getCourseFetcher(){
		return courseFetcher;
	}
	
}
