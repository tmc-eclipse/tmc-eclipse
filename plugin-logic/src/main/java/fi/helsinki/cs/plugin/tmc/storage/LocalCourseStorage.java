package fi.helsinki.cs.plugin.tmc.storage;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.IO;

public class LocalCourseStorage implements CourseDAO {

	private Gson gson;
	private IO io;
	
	private static class CoursesFileFormat {
		public List<Course> courses;
	}
	
	public LocalCourseStorage(IO io) {
		this.io = io;
		this.gson = new Gson();
	}
	
	@Override
	public List<Course> load() {
		if(!io.exists()) {
			return null;
		}
		
		Reader reader = io.getReader();
		if(reader == null) {
			return null;
		}
		
		CoursesFileFormat courseList = gson.fromJson(io.getReader(), CoursesFileFormat.class);
		
		try {
			reader.close();
		} catch(IOException e) {
		}
		
		return courseList.courses;
	}

	@Override
	public void save(List<Course> courses) {
		CoursesFileFormat courseList = new CoursesFileFormat();
		courseList.courses = courses;
		
		Writer writer = io.getWriter();
		if(writer == null) {
			return;
		}
		
		gson.toJson(courseList, writer);

		try {
			writer.close();
		} catch(IOException e) {
		}
	}
}
