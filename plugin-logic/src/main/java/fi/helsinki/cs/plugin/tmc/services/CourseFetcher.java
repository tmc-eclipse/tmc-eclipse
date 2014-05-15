package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;

public class CourseFetcher {
	private List<Course> courses;
	
	public CourseFetcher(){
		courses = new ArrayList<Course>();
		updateCourses();
	}
	
	public void updateCourses(){
		courses.clear();
		
		for(int i = 0; i<5; i++){
			courses.add(new Course("Kurssi"+i));
		}

	}
	
	public String[] getCourseNames(){
		String[] names = new String[courses.size()];
		for(int i = 0; i < names.length; i++){
			names[i] = courses.get(i).getName();
		}
		return names;
	}
	
}
