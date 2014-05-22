package fi.helsinki.cs.plugin.tmc.storage;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public interface CourseDAO {

    List<Course> load() throws UserVisibleException;

    void save(List<Course> courses) throws UserVisibleException;

}
