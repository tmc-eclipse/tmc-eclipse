package fi.helsinki.cs.tmc.core.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import fi.helsinki.cs.tmc.core.domain.SubmissionResult;
import fi.helsinki.cs.tmc.core.domain.SubmissionResult.Status;

public class SubmissionResultParserTest {

    private SubmissionResultParser parser;

    @Before
    public void setUp() {
        parser = new SubmissionResultParser();
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseFromJsonThrowsWithEmptyArgument() {
        parser.parseFromJson("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseFromJsonThrowsWithWhitespaceArgument() {
        parser.parseFromJson("          ");
    }

    @Test(expected = RuntimeException.class)
    public void parseFromJsonThrowsRuntimeExceptionOnMalformedJson() {
        parser.parseFromJson("This is invalid json");
    }

    @Test
    public void parseFromJsonReturnsValidObjectWhenDeserializing() {
        SubmissionResult initial = new SubmissionResult();
        initial.setError("Error");
        initial.setStatus(Status.OK);
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        initial.setMissingReviewPoints(list);

        Gson gson = new Gson();
        SubmissionResult result = parser.parseFromJson(gson.toJson(initial));
        assertEquals(initial.getError(), result.getError());
        assertEquals(initial.getStatus(), result.getStatus());
        assertEquals(initial.getMissingReviewPoints(), result.getMissingReviewPoints());
    }
}
