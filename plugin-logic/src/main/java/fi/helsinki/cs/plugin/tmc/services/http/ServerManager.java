package fi.helsinki.cs.plugin.tmc.services.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.CourseList;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.ExerciseList;

public class ServerManager {
    private ConnectionBuilder connectionBuilder;
    private Gson mapper;

    public ServerManager(Gson mapper, ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
        this.mapper = mapper;
    }

    public ServerManager() {
        this(new Gson(), new ConnectionBuilder());
    }

    public List<Course> getCourses() {
        String url = connectionBuilder.getUrl(UrlExtension.COURSES.getExtension());
        String bodyText = getString(url);
        CourseList cl = mapper.fromJson(bodyText, CourseList.class);
        if (cl == null) {
            return new ArrayList<Course>();
        }
        return Arrays.asList(cl.getCourses());
    }

    public List<Exercise> getExercises(String courseId) {
        String url = connectionBuilder.getUrl(UrlExtension.EXERCISES.getExtension(courseId));
        String bodyText = getString(url);
        ExerciseList el = mapper.fromJson(bodyText, ExerciseList.class);
        List<Exercise> exercises = el.getExercises();

        // convert date string to Date object
        for (Exercise e : exercises) {
            e.finalizeDeserialization();
        }
        return exercises;
    }

    public ZippedProject getExerciseZip(String zipUrl) {
        ZippedProject zip = new ZippedProject();
        try {
            byte[] bytes = getBytes(zipUrl);
            zip.setBytes(bytes);
        } catch (Exception e) {
            // TODO: Better error handling?
            e.printStackTrace();
        }
        return zip;
    }

    private byte[] getBytes(String url) {
        byte[] bytes;
        try {
            bytes = connectionBuilder.createConnection().getForBinary(url);
        } catch (Exception e) {
            // TODO: Better error handling?
            bytes = new byte[0];
        }
        return bytes;
    }

    private String getString(String url) {
        String json;
        try {
            json = connectionBuilder.createConnection().getForText(url);
            System.out.println(url);
        } catch (Exception e) {
            // TODO: Better errorhandling?
            e.printStackTrace();
            json = "";
        }
        return json;
    }
}
