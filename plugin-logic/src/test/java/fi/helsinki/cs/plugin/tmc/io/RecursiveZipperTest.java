package fi.helsinki.cs.plugin.tmc.io;

import static org.junit.Assert.assertEquals;

import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.io.zipper.RecursiveZipper;
import fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider.ZIP_ALL_THE_THINGS;

public class RecursiveZipperTest {

    private String path;

    @Before
    public void setUp() {
        this.path = "src/test/java/fi/helsinki/cs/plugin/tmc/io/";
    }

    @Test
    public void testZippingDirectory() throws Exception {
        zipDirectory(new FileIO(path + "testDirectory"), "testDirectoryOutput.zip");

        ZipFile original = new ZipFile(path + "testDirectory.zip");
        ZipFile zipped = new ZipFile(path + "testDirectoryOutput.zip");

        Set<Long> originalContents = new LinkedHashSet<Long>();
        for (Enumeration<?> e = original.entries(); e.hasMoreElements();) {
            originalContents.add(((ZipEntry) e.nextElement()).getCrc());
        }

        Set<Long> zippedContents = new LinkedHashSet<Long>();
        for (Enumeration<?> e = zipped.entries(); e.hasMoreElements();) {
            zippedContents.add(((ZipEntry) e.nextElement()).getCrc());
        }

        original.close();
        zipped.close();

        assertEquals(originalContents, zippedContents);
    }

    private void zipDirectory(IO directory, String zipName) throws Exception {
        RecursiveZipper zipper = new RecursiveZipper(directory, new ZIP_ALL_THE_THINGS());

        byte[] zip = zipper.zipProjectSources();

        OutputStream os = new FileIO(path + zipName).getOutputStream();
        try {
            IOUtils.write(zip, os);
        } finally {
            os.close();
        }
    }

}
