package fi.helsinki.cs.plugin.tmc.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;

public class UnzipperTest {
	private String path;

	@Before
	public void setUp() {
		this.path = "src/test/java/fi/helsinki/cs/plugin/tmc/io/";
	}

	@Test
	public void unzipTestZip() throws FileNotFoundException, IOException {
		File f = new File(path + "testZip.zip");
		byte[] b = IOUtils.toByteArray(new FileInputStream(f));
		ZippedProject project = new ZippedProject();
		Unzipper unzipper = new Unzipper(project);
		project.setBytes(b);
		unzipper.unzipTo(new FileIO(path));

		Scanner scanner = new Scanner(new File(path + "testFile.txt"));

		assertEquals("This is a test. ", scanner.nextLine());
		assertEquals(false, scanner.hasNextLine());
		scanner.close();
	}

	@Test
	public void unzippingCanCreateFolders() throws FileNotFoundException, IOException {
		File f = new File(path + "testDirectory.zip");
		byte[] b = IOUtils.toByteArray(new FileInputStream(f));
		ZippedProject project = new ZippedProject();
		project.setBytes(b);
		Unzipper unzipper = new Unzipper(project);
		unzipper.unzipTo(new FileIO(path));
		
		Scanner scanner = new Scanner(new File(path + "testDirectory/testFile.txt"));
		
		for (int i = 0; i < 5000; i++){
			assertEquals("This is a test. ", scanner.nextLine());
		}
		assertEquals(false, scanner.hasNextLine());
		scanner.close();
		
		assertEquals(true, new File(path + "testDirectory/dir").exists());
		assertEquals(true, new File(path + "testDirectory/dir").isDirectory());
	}

}
