package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Settings;

import java.util.ArrayList;
import java.util.List;

public class Core {

private List<Course> courses;
private Settings settings;
private CourseFetcher courseFetcher;

	public Core() {
		
		settings = Settings.getDefaultSettings();
		courseFetcher = new CourseFetcher();
		generateDummyCourses();
		
		
	}
	
	private void generateDummyCourses(){
		courses = new ArrayList<Course>();
		for(int i = 0; i < 5; i++){
			courses.add(new Course("Kurssi"+i));
		}
	}
	
	public Settings getSettings(){
		return settings;
	}
	
	public CourseFetcher getCourseFetcher(){
		return courseFetcher;
	}
	
}
