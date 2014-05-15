package fi.helsinki.cs.plugin.tmc.storage;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;

public interface CourseDAO {

	public List<Course> load();
	public void save(List<Course> courses);
	
}
