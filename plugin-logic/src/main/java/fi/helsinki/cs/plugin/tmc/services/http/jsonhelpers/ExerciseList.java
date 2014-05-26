package fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;

public class ExerciseList {

    @SerializedName("api_version")
    private String apiVersion;

    @SerializedName("course")
    private Course course;

    public List<Exercise> getExercises() {
        for (Exercise e : course.getExercises()) {
            e.setCourseName(course.getName());
        }

        return course.getExercises();
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
