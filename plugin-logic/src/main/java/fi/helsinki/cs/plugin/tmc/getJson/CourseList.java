package fi.helsinki.cs.plugin.tmc.getJson;

import fi.helsinki.cs.plugin.tmc.domain.*;
import com.google.gson.annotations.SerializedName;

public class CourseList {
	@SerializedName("api_version")
	private String apiVersion;
	@SerializedName("courses")
	private Course[] courses;
	
	public String getApiVersion(){
		return apiVersion;
	}
	
	public Course[] getCourses(){
		return courses;
	}
}
