package fi.helsinki.cs.plugin.tmc.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;

public class Unzipper {

    private ZippedProject project;

    public Unzipper(ZippedProject project) {
        this.project = project;
    }

    public List<String> unzipTo(IO destinationFolder) throws IOException {
        List<String> projectFiles = new ArrayList<String>();

        destinationFolder.createFolderTree(false);
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(project.getBytes()));
        ZipEntry zipEntry = zipStream.getNextEntry();

        while (zipEntry != null) {
            String entryPath = destinationFolder.getPath() + File.separator + zipEntry.getName();
            projectFiles.add(entryPath);

            FileIO file = new FileIO(entryPath);
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

        return projectFiles;
    }

}
