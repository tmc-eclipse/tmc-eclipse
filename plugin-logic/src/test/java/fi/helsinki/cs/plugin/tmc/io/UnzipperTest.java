package fi.helsinki.cs.plugin.tmc.io;

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

import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.zipper.Unzipper;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.PreventFolderOverwriteUnzippingDecider;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UNZIP_ALL_THE_THINGS;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UnzippingDecider;

public class UnzipperTest {
    private String path;

    @Before
    public void setUp() {
        this.path = "src/test/java/fi/helsinki/cs/plugin/tmc/io/";
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

    @Test
    public void filesAreUnzippedCorretlyWhenUsingZippingDecider() throws FileNotFoundException, IOException {
        try {
            unZipDirectory("unzippingdecider_original.zip");
            unZipDirectory("unzippingdecider_overwriter.zip",
                    new PreventFolderOverwriteUnzippingDecider("no_overwrite"));
            assertFileOk("unzippingdecider/doc", "This is a test");
            assertFileOk("unzippingdecider/foo/doc", "This is a test");
            assertFileOk("unzippingdecider/foo/no_overwrite/doc", "Should not be overwritten");
            assertFileOk("unzippingdecider/foo/no_overwrite/new_doc", "This should be unzipped");
            assertFileOk("unzippingdecider/foo/no_overwrite/bar/doc", "Should not be overwritten");
        } finally {
            FileUtils.deleteDirectory(new File(path + "unzippingdecider/"));
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
