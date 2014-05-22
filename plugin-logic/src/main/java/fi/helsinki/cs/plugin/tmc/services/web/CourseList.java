package fi.helsinki.cs.plugin.tmc.services.web;

import com.google.gson.annotations.SerializedName;

import fi.helsinki.cs.plugin.tmc.domain.Course;

public class CourseList {
    @SerializedName("api_version")
    private String apiVersion;
    @SerializedName("courses")
    private Course[] courses;

    public String getApiVersion() {
        return apiVersion;
    }

    public Course[] getCourses() {
        return courses;
    }
}
