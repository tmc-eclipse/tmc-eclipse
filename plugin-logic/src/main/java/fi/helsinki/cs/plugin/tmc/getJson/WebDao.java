package fi.helsinki.cs.plugin.tmc.getJson;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;

public class WebDao {
	private JsonGetter getter;
	private Gson mapper;
	
	public WebDao(){
		this.getter = new JsonGetter();
		this.mapper = new Gson();
	}
	
	public List<Course> getCourses(){
		String bodyText = getter.getJson(UrlExtension.COURSES);
		CourseList cl = mapper.fromJson(bodyText, CourseList.class);
		return Arrays.asList(cl.getCourses());
	}
}
