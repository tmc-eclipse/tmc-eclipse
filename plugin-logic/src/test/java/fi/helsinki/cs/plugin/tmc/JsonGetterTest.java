package fi.helsinki.cs.plugin.tmc;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonParser;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.getJson.CourseList;
import fi.helsinki.cs.plugin.tmc.getJson.JsonGetter;
import fi.helsinki.cs.plugin.tmc.getJson.WebDao;

public class JsonGetterTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		WebDao dao = new WebDao();
		List<Course> list = dao.getCourses();
		for (Course c : list) {
			System.out.println(c);
		}
		List<Exercise> list2 = dao.getExercises("2");
		for (Exercise e : list2){
			System.out.println(e);
		}
	}
}
