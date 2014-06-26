package fi.helsinki.cs.tmc.core.io.zip;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.ZippedProject;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.io.zip.Unzipper;
import fi.helsinki.cs.tmc.core.io.zip.unzippingdecider.UNZIP_ALL_THE_THINGS;
import fi.helsinki.cs.tmc.core.io.zip.unzippingdecider.UnzippingDecider;

public class UnzipperTest {
    private String path;

    @Before
    public void setUp() {
        this.path = "src/test/java/fi/helsinki/cs/tmc/core/io/";
    }

    @Test
    public void unzipTestZip() throws FileNotFoundException, IOException {
        File f = new File(path + "testZip.zip");
        FileInputStream s = new FileInputStream(f);

        byte[] b = IOUtils.toByteArray(s);
        s.close();

        ZippedProject project = new ZippedProject();
        Unzipper unzipper = new Unzipper(project, new UNZIP_ALL_THE_THINGS());
        project.setBytes(b);

        unzipper.unzipTo(new FileIO(path));

        f = new File(path + "testFile.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(f);

            assertEquals("This is a test. ", scanner.nextLine());
            assertEquals(false, scanner.hasNextLine());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            f.delete();
        }
    }

    @Test
    public void unzippingCanCreateFolders() throws FileNotFoundException, IOException {
        unZipDirectory("testDirectory.zip");

        File f = new File(path + "testDirectory/testFile.txt");
        Scanner scanner = null;
        try {
            assertEquals(true, f.exists());
            scanner = new Scanner(f);

            for (int i = 0; i < 5000; i++) {
                assertEquals("This is a test. ", scanner.nextLine());
            }
            assertEquals(false, scanner.hasNextLine());
            f = new File(path + "testDirectory/dir");
            assertEquals(true, f.exists());
            assertEquals(true, f.isDirectory());

        } finally {
            if (scanner != null) {
                scanner.close();
            }
            FileUtils.deleteDirectory(new File(path + "testDirectory/"));
        }
    }

    @Test
    public void unzippingAFolderDoesNotOverWriteFilesInExistingFolder() throws FileNotFoundException, IOException {
        unZipDirectory("testDirectory.zip");
        unZipDirectory("overWriteTest.zip");
        try {
            File f = new File(path + "testDirectory/eclipse");
            assertEquals(true, f.exists());
            assertEquals(false, f.isDirectory());

            f = new File(path + "testDirectory/testFile.txt");
            assertEquals(true, f.exists());
            assertEquals(false, f.isDirectory());

            f = new File(path + "testDirectory/dir");
            assertEquals(true, f.exists());
            assertEquals(true, f.isDirectory());

            f = new File(path + "testDirectory");
            assertEquals(true, f.exists());
            assertEquals(true, f.isDirectory());
        } finally {
            FileUtils.deleteDirectory(new File(path + "testDirectory/"));
        }

    }

    private void assertFileOk(String file, String content) throws FileNotFoundException {
        File f = new File(path + file);
        Scanner scanner = null;
        try {
            assertEquals(true, f.exists());
            assertEquals(false, f.isDirectory());
            scanner = new Scanner(f);
            assertEquals(content, scanner.nextLine());
        } finally {
            scanner.close();
        }
    }

    private void unZipDirectory(String zip) throws IOException, FileNotFoundException {
        unZipDirectory(zip, new UNZIP_ALL_THE_THINGS());
    }

    private void unZipDirectory(String zip, UnzippingDecider decider) throws IOException, FileNotFoundException {
        File f = new File(path + zip);
        byte[] b = IOUtils.toByteArray(new FileInputStream(f));
        ZippedProject project = new ZippedProject();
        project.setBytes(b);
        Unzipper unzipper = new Unzipper(project, decider);
        unzipper.unzipTo(new FileIO(path));
    }

}
