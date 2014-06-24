package fi.helsinki.cs.tmc.core.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

import fi.helsinki.cs.tmc.core.domain.ClassPath;
import fi.helsinki.cs.tmc.core.io.FileUtil;

public class ClassPathTest {
    private ClassPath classpath;
    File root;

    @Before
    public void setUp() {
        root = Files.createTempDir();
        File deepSubDir = new File(root.getAbsolutePath() + "/sub/subsub/subsubsub");
        deepSubDir.mkdirs();

        classpath = new ClassPath(FileUtil.getUnixPath(root.getAbsolutePath().toString() + "/*"));
    }

    @Test
    public void addingSamePathTwiceOnlyAddsItOnce() {
        classpath.add("a");
        classpath.add("a");
        assertEquals(2, classpath.subPaths.size());
    }

    @Test
    public void addingACompleteClasspathAddsAllItsSubpaths() {
        ClassPath another = new ClassPath("another1");
        another.add("another2");

        classpath.add(another);
        assertTrue(classpath.subPaths.contains("another1"));
        assertTrue(classpath.subPaths.contains("another2"));
    }

    @Test
    public void addDirAndSubDirsAddsAllSubDirs() {
        classpath.addDirAndSubDirs(FileUtil.getUnixPath(root.getAbsolutePath().toString()) + "/sub/");
        String rootPath = FileUtil.getUnixPath(root.getAbsolutePath());
        assertTrue(classpath.getSubPaths().contains(rootPath + "/sub/*"));
        assertTrue(classpath.getSubPaths().contains(rootPath + "/sub/subsub/*"));
        assertTrue(classpath.getSubPaths().contains(rootPath + "/sub/subsub/subsubsub/*"));
    }

    @Test
    public void providingFileToAddDirAndSubDirsUsesThatFilesParentDirectoryInstead() throws IOException {
        String rootPath = FileUtil.getUnixPath(root.getAbsolutePath());
        File tmpFile = new File(rootPath + "/sub/foo.txt");
        tmpFile.createNewFile();

        classpath.addDirAndSubDirs(rootPath + "/sub/foo.txt");
        
        assertTrue(classpath.getSubPaths().contains(rootPath + "/sub/*"));
        assertTrue(classpath.getSubPaths().contains(rootPath + "/sub/subsub/*"));
        assertTrue(classpath.getSubPaths().contains(rootPath + "/sub/subsub/subsubsub/*"));
    }

    @Test
    public void addDirAndSubDirsDoesNotAddThingsMultipleTimes() {
        classpath.addDirAndSubDirs(root.getAbsolutePath().toString() + "/sub/");
        classpath.addDirAndSubDirs(root.getAbsolutePath().toString() + "/sub/");
        assertEquals(4, classpath.subPaths.size());
    }

    @Test
    public void toStringReturnsCorrectlyAppendsFilesToString() {
        String rootPath = FileUtil.getUnixPath(root.getAbsolutePath());

        classpath.addDirAndSubDirs(FileUtil.getUnixPath(root.getAbsolutePath().toString()) + "/sub/");
        String expected = rootPath + "/*" + System.getProperty("path.separator") + rootPath + "/sub/*"
                + System.getProperty("path.separator") + rootPath + "/sub/subsub/*"
                + System.getProperty("path.separator") + rootPath + "/sub/subsub/subsubsub/*";
        assertEquals(expected, classpath.toString());
    }
}
