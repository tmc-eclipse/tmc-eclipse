package fi.helsinki.cs.plugin.tmc.io.zipper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UnzippingDecider;

public class Unzipper {

    private static final int BUFFER_SIZE = 1024;

    private ZippedProject project;
    private UnzippingDecider decider;

    public Unzipper(ZippedProject project, UnzippingDecider decider) {
        this.project = project;
        this.decider = decider;
    }

    public List<String> unzipTo(IO destinationFolder) throws IOException {
        List<String> projectFiles = new ArrayList<String>();

        destinationFolder.createFolderTree(false);
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(project.getBytes()));
        ZipEntry zipEntry = zipStream.getNextEntry();

        while (zipEntry != null) {
            String entryPath = destinationFolder.getPath() + File.separator + zipEntry.getName();

            if (!decider.shouldUnzip(entryPath)) {
                zipEntry = zipStream.getNextEntry();
                continue;
            }

            projectFiles.add(entryPath);

            FileIO file = new FileIO(entryPath);
            file.createFolderTree(!zipEntry.isDirectory());

            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int read = 0;
            while ((read = zipStream.read(buffer)) != -1) {
                stream.write(buffer, 0, read);
                buffer = new byte[BUFFER_SIZE];
            }

            if (!zipEntry.isDirectory()) {
                file.write(stream.toByteArray());
            }

            zipEntry = zipStream.getNextEntry();
        }

        return projectFiles;
    }

}
