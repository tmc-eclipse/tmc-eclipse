package fi.helsinki.cs.plugin.tmc.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fi.helsinki.cs.plugin.tmc.services.ZippedProject;

public class Unzipper {

	private ZippedProject project;
	
	public Unzipper(ZippedProject project) {
		this.project = project;
	}
	
	public void unzipTo(IO destinationFolder) throws IOException {
		destinationFolder.createFolderTree(false);
		
		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(project.getBytes()));
		
		ZipEntry zipEntry = zipStream.getNextEntry();
		
		while(zipEntry != null) {	
			FileIO file = new FileIO(destinationFolder.getPath() + File.separator + zipEntry.getName());
			file.createFolderTree(!zipEntry.isDirectory());
			
			byte[] unzippedBytes = new byte[(int)zipEntry.getSize()];
			int returnVal = zipStream.read(unzippedBytes, 0, (int) zipEntry.getSize());

			file.write(unzippedBytes);
			
			zipEntry = zipStream.getNextEntry();
		}
	}
	
}
