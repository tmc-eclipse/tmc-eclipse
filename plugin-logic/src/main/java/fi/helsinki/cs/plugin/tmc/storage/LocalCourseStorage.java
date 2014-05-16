package fi.helsinki.cs.plugin.tmc.storage;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.ExerciseKey;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.services.web.UserVisibleException;

public class LocalCourseStorage implements CourseDAO {

	private Gson gson;
	private IO io;
	
	private static class CoursesFileFormat {
		public List<Course> courses;
	}
	
	public LocalCourseStorage(IO io) {
		this.io = io;
		this.gson = new GsonBuilder()
			.serializeNulls()
			.setPrettyPrinting()
			.registerTypeAdapter(ExerciseKey.class, new ExerciseKey.GsonAdapter())
			.create();
	}
	
	@Override
	public List<Course> load() throws UserVisibleException {
		if(!io.exists()) {
			throw new UserVisibleException("Could not load course data from local storage.");
		}
		
		Reader reader = io.getReader();
		if(reader == null) {
			throw new UserVisibleException("Could not load course data from local storage.");
		}
		
		CoursesFileFormat courseList = gson.fromJson(io.getReader(), CoursesFileFormat.class);
		
		try {
			reader.close();
		} catch(IOException e) {
		}
		
		return courseList.courses;
	}

	@Override
	public void save(List<Course> courses) throws UserVisibleException {
		CoursesFileFormat courseList = new CoursesFileFormat();
		courseList.courses = courses;
		
		if(io == null){
			throw new UserVisibleException("Could not save course data to local storage.");
		}
		
		Writer writer = io.getWriter();
		if(writer == null) {
			throw new UserVisibleException("Could not save course data to local storage.");
		}
		
		gson.toJson(courseList, writer);
		try {
			writer.close();
		} catch(IOException e) {}
	}
	
}