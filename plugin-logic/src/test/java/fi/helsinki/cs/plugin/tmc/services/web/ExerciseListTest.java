package fi.helsinki.cs.plugin.tmc.services.web;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;

public class ExerciseListTest {

    @Test
    public void canSerializeInto() {
        String json = "{\"api_version\":7,\"course\":{\"id\":6,\"name\":\"cpro\",\"details_url\":\"http://tmc.mooc.fi/test/courses/6.json\",\"unlock_url\":\"http://tmc.mooc.fi/test/courses/6/unlock.json\",\"reviews_url\":\"http://tmc.mooc.fi/test/courses/6/reviews.json\",\"comet_url\":\"http://tmc.mooc.fi:8080/comet\",\"spyware_urls\":[\"http://staging.spyware.testmycode.net/\"],\"unlockables\":[],\"exercises\":[{\"id\":553,\"name\":\"week1-PolynomialOperations\",\"locked\":false,\"deadline_description\":\"2013-09-21 07:00:00 +0300\",\"deadline\":\"2013-09-21T07:00:00+03:00\",\"checksum\":\"661ffdd5d5bcd1020f15787a4b1f7f17\",\"return_url\":\"http://tmc.mooc.fi/test/exercises/553/submissions.json\",\"zip_url\":\"http://tmc.mooc.fi/test/exercises/553.zip\",\"returnable\":true,\"requires_review\":false,\"attempted\":false,\"completed\":false,\"reviewed\":false,\"all_review_points_given\":true,\"memory_limit\":null,\"solution_zip_url\":\"http://tmc.mooc.fi/test/exercises/553/solution.zip\",\"exercise_submissions_url\":\"http://tmc.mooc.fi/test/exercises/553.json?api_version=7\"},{\"id\":554,\"name\":\"week1-SimpleMath\",\"locked\":false,\"deadline_description\":\"2013-09-21 07:00:00 +0300\",\"deadline\":\"2013-09-21T07:00:00+03:00\",\"checksum\":\"a966993239920ecb14ffb11860046cf8\",\"return_url\":\"http://tmc.mooc.fi/test/exercises/554/submissions.json\",\"zip_url\":\"http://tmc.mooc.fi/test/exercises/554.zip\",\"returnable\":true,\"requires_review\":false,\"attempted\":false,\"completed\":false,\"reviewed\":false,\"all_review_points_given\":true,\"memory_limit\":null,\"solution_zip_url\":\"http://tmc.mooc.fi/test/exercises/554/solution.zip\",\"exercise_submissions_url\":\"http://tmc.mooc.fi/test/exercises/554.json?api_version=7\"},{\"id\":555,\"name\":\"week1-WritingAndReading\",\"locked\":false,\"deadline_description\":\"2013-09-21 07:00:00 +0300\",\"deadline\":\"2013-09-21T07:00:00+03:00\",\"checksum\":\"947a524eb4e81f194e3ccb8a5ac0bbb8\",\"return_url\":\"http://tmc.mooc.fi/test/exercises/555/submissions.json\",\"zip_url\":\"http://tmc.mooc.fi/test/exercises/555.zip\",\"returnable\":true,\"requires_review\":false,\"attempted\":false,\"completed\":false,\"reviewed\":false,\"all_review_points_given\":true,\"memory_limit\":null,\"solution_zip_url\":\"http://tmc.mooc.fi/test/exercises/555/solution.zip\",\"exercise_submissions_url\":\"http://tmc.mooc.fi/test/exercises/555.json?api_version=7\"}]}}";
        ExerciseList exerciseList = new Gson().fromJson(json, ExerciseList.class);
        assertNotNull(exerciseList.getExercises());
    }

}
