package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.domain.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.getJson.CourseList;
import fi.helsinki.cs.plugin.tmc.getJson.JsonGetter;

public class JsonGetterTest {
	JsonGetter getter;

	@Before
	public void setUp() throws Exception {
		getter = new JsonGetter();
	}

	@Test
	public void test() {
		CourseList list = getter.getCourses();
		assertTrue(list != null);
		System.out.println(list.getApiVersion());
		
		for (Course c: list.getCourses()) {
			System.out.println(c);
		}
	}
}
