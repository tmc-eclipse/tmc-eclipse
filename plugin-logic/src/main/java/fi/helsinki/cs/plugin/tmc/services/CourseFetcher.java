package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.services.web.WebDao;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class CourseFetcher {
	
	private Courses courses;
	
	public CourseFetcher(Courses courses) {
		this.courses = courses;
	}
	
	public void updateCourses() {
		WebDao webDao = new WebDao();
		try {
			courses.setCourses(webDao.getCourses());
		} catch(UserVisibleException e) {
			// TODO: show error message to user
		}
	}
	
	public String[] getCourseNames() {
		List<Course> courseList = courses.getCourses();
		String[] names = new String[courseList.size()];
		
		for(int i = 0; i < courseList.size(); i++) {
			names[i] = courseList.get(i).getName();
		}
		
		return names;
	}
	
}
