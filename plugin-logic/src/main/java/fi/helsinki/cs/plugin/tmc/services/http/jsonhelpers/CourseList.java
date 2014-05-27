package fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers;

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

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }
}
