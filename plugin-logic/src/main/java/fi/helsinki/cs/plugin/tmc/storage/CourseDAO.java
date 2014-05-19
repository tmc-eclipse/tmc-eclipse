package fi.helsinki.cs.plugin.tmc.storage;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public interface CourseDAO {

	public List<Course> load() throws UserVisibleException;
	public void save(List<Course> courses) throws UserVisibleException;
	
}
