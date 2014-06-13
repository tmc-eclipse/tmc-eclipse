package fi.helsinki.cs.plugin.tmc.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClassPathTest {
    private ClassPath classpath;

    @Before
    public void setUp() {
        this.classpath = new ClassPath("classpath1");
        this.classpath.add("classpath2");
        this.classpath.add("classpath3");
        this.classpath.add("classpath2");
        this.classpath.add(new ClassPath("subClasspath"));
    }

    @Test
    public void subPathsTest() {
        assertEquals(classpath.getSubPaths().size(), 4);
        assertEquals(classpath.getSubPaths().get(1), "classpath2");
    }
    
    @Test
    public void toStringTest() {
        assertEquals(classpath.toString(), "classpath1"+System.getProperty("path.separator")+"classpath2"+System.getProperty("path.separator")+"classpath3"+System.getProperty("path.separator")+"subClasspath");
        classpath = new ClassPath("");
        classpath.getSubPaths().remove(0);
        assertEquals(classpath.toString(), "");
    }

}
