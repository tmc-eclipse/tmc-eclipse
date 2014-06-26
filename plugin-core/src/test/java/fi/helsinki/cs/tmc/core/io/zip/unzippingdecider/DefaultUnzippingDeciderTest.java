package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Project;

public class DefaultUnzippingDeciderTest {

    private Project project;

    @Before
    public void setUp() {
        project = mock(Project.class);
    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
