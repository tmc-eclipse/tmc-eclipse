package fi.helsinki.cs.tmc.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

public class ServiceFactoryTest {

    // this test has nasty dependencies to actual files due to various DAOS
    // trying to actually load info from disk, hence it's disabled
    @Ignore
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
