package fi.helsinki.cs.plugin.tmc.services.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;

public class WebDao {
    private JsonGetter getter;
    private Gson mapper;

    public WebDao(Gson mapper, JsonGetter getter) {
        this.getter = getter;
        this.mapper = mapper;
    }

    public List<Course> getCourses() {
        String bodyText = getter.getJson(UrlExtension.COURSES.getExtension());
        CourseList cl = mapper.fromJson(bodyText, CourseList.class);
        if (cl == null) {
            return new ArrayList<Course>();
        }
        return Arrays.asList(cl.getCourses());
    }

    public List<Exercise> getExercises(String courseId) {
        String bodyText = getter.getJson(UrlExtension.EXERCISES.getExtension(courseId));
        ExerciseList el = mapper.fromJson(bodyText, ExerciseList.class);
        List<Exercise> exercises = el.getExercises();

        // convert date string to Date object
        for (Exercise e : exercises) {
            e.finalizeDeserialization();
        }
        return exercises;
    }
}
