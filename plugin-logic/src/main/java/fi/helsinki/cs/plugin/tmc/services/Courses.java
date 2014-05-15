package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.storage.CourseDAO;

public class Courses {

	private CourseDAO dao;
	private List<Course> courses;
	
	public Courses(CourseDAO dao) {
		this.dao = dao;
		this.courses = dao.load();
	}
	
	public List<Course> getCourses() {
		return courses;
	}
	
	public void setCourses(List<Course> courses) {
		this.courses = courses;
		dao.save(courses);
	}
	
	public void updateCourse(Course course) {
		for(int i = 0; i < courses.size(); i++) {
			if(courses.get(i).getName().equals(course.getName())) {
				courses.set(i, course);
				break;
			}
		}
		dao.save(courses);
	}
	
	public Course getCourseByName(String name) {
		for(Course course : courses) {
			if(course.getName().equals(name)) {
				return course;
			}
		}
		return null;
	}
	
}
