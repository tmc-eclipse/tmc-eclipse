package fi.helsinki.cs.plugin.tmc;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ServiceFactoryTest {

    @Test
    public void constructorInitialisesEverythingCorrectly() {
        ServiceFactory serviceFactory = new ServiceFactoryImpl();
        assertNotNull(serviceFactory.getSettings());
        assertNotNull(serviceFactory.getCourseDAO());
        assertNotNull(serviceFactory.getProjectDAO());
        assertNotNull(serviceFactory.getProjectEventHandler());
        assertNotNull(serviceFactory.getServerManager());
        assertNotNull(serviceFactory.getSpyware());
        assertNotNull(serviceFactory.getUpdater());
        assertNotNull(serviceFactory.getReviewDAO());
    }

}
