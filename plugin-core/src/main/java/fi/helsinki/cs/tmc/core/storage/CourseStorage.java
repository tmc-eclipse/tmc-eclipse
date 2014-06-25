package fi.helsinki.cs.tmc.core.storage;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.ExerciseKey;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.storage.formats.CoursesFileFormat;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

public class CourseStorage implements DataSource<Course> {

    private Gson gson;
    private FileIO io;

    public CourseStorage(FileIO io) {
        this.io = io;
        this.gson = createGson();
    }

    @Override
    public List<Course> load() {
        if (!io.fileExists()) {
            return new ArrayList<Course>();
        }
        CoursesFileFormat courseList = null;
        Reader reader = io.getReader();
        if (reader == null) {
            throw new UserVisibleException("Could not load course data from local storage.");
        }
        try {
            courseList = gson.fromJson(reader, CoursesFileFormat.class);
        } catch (JsonSyntaxException ex) {
            throw new UserVisibleException("Local course storage corrupted");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                return getCourses(courseList);
            }
        }

        return getCourses(courseList);
    }

    private List<Course> getCourses(CoursesFileFormat courseList) {
        if (courseList != null) {
            return courseList.getCourses();
        } else {
            return new ArrayList<Course>();
        }
    }

    @Override
    public void save(List<Course> courses) {
        CoursesFileFormat courseList = new CoursesFileFormat();
        courseList.setCourses(courses);

        if (io == null) {
            throw new UserVisibleException("Could not save course data to local storage.");
        }

        Writer writer = io.getWriter();
        if (writer == null) {
            throw new UserVisibleException("Could not save course data to local storage.");
        }

        gson.toJson(courseList, writer);
        try {
            writer.close();
        } catch (IOException e) {
            // TODO: Log here?
            return;
        }
    }

    private Gson createGson() {
        return new GsonBuilder().serializeNulls().setPrettyPrinting()
                .registerTypeAdapter(ExerciseKey.class, new ExerciseKey.GsonAdapter()).create();
    }

}
