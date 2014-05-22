package fi.helsinki.cs.plugin.tmc.services.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

public class CourseListTest {

    @Test
    public void canDeserializeInto() {
        CourseList courseList = new Gson().fromJson("{\"api_version\": \"7.0\", \"courses\": []}", CourseList.class);
        assertEquals("7.0", courseList.getApiVersion());
        assertEquals(0, courseList.getCourses().length);
    }

}
