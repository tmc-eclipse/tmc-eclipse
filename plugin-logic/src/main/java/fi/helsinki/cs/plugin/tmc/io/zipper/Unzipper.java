package fi.helsinki.cs.plugin.tmc.io.zipper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.IO;

public class Unzipper {

    private ZippedProject project;

    public Unzipper(ZippedProject project) {
        this.project = project;
    }

    public void unzipTo(IO destinationFolder) throws IOException {
        destinationFolder.createFolderTree(false);

        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(project.getBytes()));

        ZipEntry zipEntry = zipStream.getNextEntry();

        while (zipEntry != null) {
            FileIO file = new FileIO(destinationFolder.getPath() + File.separator + zipEntry.getName());
            file.createFolderTree(!zipEntry.isDirectory());

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int read = 0;
            while ((read = zipStream.read(buffer)) != -1) {
                stream.write(buffer, 0, read);
                buffer = new byte[1024];
            }

            if (!zipEntry.isDirectory()) {
                file.write(stream.toByteArray());
            }

            zipEntry = zipStream.getNextEntry();
        }
    }

}
