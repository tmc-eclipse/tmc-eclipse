package fi.helsinki.cs.plugin.tmc.services.web;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;

public class ExerciseList {
	
	@SerializedName("api_version")
	private String apiVersion;
	
	@SerializedName("course")
	private Course course;

	
	public List<Exercise> getExercises(){
		return course.getExercises();
	}
	
}
