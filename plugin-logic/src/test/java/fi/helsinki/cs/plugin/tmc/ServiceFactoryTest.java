package fi.helsinki.cs.plugin.tmc;

import static org.junit.Assert.*;

import org.junit.Test;

public class ServiceFactoryTest {

	@Test
	public void constructorInitialisesEverythingCorrectly(){
		ServiceFactory serviceFactory = new ServiceFactory();
		assertNotNull(serviceFactory.getSettings());
		assertNotNull(serviceFactory.getCourses());
		assertNotNull(serviceFactory.getCourseFetcher());
		assertNotNull(serviceFactory.getExerciseFetcher());
	}

}
