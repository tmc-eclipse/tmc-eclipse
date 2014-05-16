package fi.helsinki.cs.plugin.tmc.services.web;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;


public class WebDao {
	private JsonGetter getter;
	private Gson mapper;
	
	public WebDao(){
		this.getter = new JsonGetter();
		this.mapper = new Gson();
	}
	
	public List<Course> getCourses(){
		String bodyText = getter.getJson(UrlExtension.COURSES.getExtension());
		CourseList cl = mapper.fromJson(bodyText, CourseList.class);
		return Arrays.asList(cl.getCourses());
	}
	
	public List<Exercise> getExercises(String courseId) {
		String bodyText = getter.getJson(UrlExtension.EXERCISES.getExtension(courseId));
		ExerciseList el = mapper.fromJson(bodyText, ExerciseList.class);
		return el.getExercises();
	}
}
