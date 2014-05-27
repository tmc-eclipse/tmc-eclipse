package fi.helsinki.cs.plugin.tmc.io.zipper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.io.zipper.zippingdecider.ZippingDecider;

public class RecursiveZipper {

    private IO rootDirectory;
    private ZippingDecider zippingDecider;

    public RecursiveZipper(IO rootDirectory, ZippingDecider zippingDecider) {
        this.rootDirectory = rootDirectory;
        this.zippingDecider = zippingDecider;
    }

    /**
     * Zip up a project directory, only including stuff decided by the
     * {@link ZippingDecider}.
     */
    public byte[] zipProjectSources() throws IOException {
        if (!rootDirectory.directoryExists()) {
            throw new FileNotFoundException("Root directory " + rootDirectory.getPath() + " not found for zipping!");
        }

        ByteArrayOutputStream zipBuffer = new ByteArrayOutputStream();
        ZipOutputStream zipStream = new ZipOutputStream(zipBuffer);

        try {
            zipRecursively(rootDirectory, zipStream, "");
        } finally {
            zipStream.close();
        }
        return zipBuffer.toByteArray();
    }

    private void writeEntry(IO file, ZipOutputStream zipStream, String zipPath) throws IOException {
        zipStream.putNextEntry(new ZipEntry(zipPath + File.separator + file.getName()));

        InputStream in = file.getInputStream();
        try {
            IOUtils.copy(in, zipStream);
        } finally {
            in.close();

        }

        zipStream.closeEntry();
    }

    /**
     * Zips a directory recursively.
     */
    private void zipRecursively(IO directory, ZipOutputStream zipStream, String parentZipPath) throws IOException {
        String thisDirZipPath;
        if (parentZipPath.isEmpty()) {
            thisDirZipPath = directory.getName();
        } else {
            thisDirZipPath = parentZipPath + File.separator + directory.getName();
        }

        // Create an entry for the directory
        zipStream.putNextEntry(new ZipEntry(thisDirZipPath + File.separator));
        zipStream.closeEntry();

        for (IO file : directory.getChildren()) {
            try {
                boolean isDirectory = file.directoryExists();

                String zipPath = thisDirZipPath + File.separator + file.getName();
                if (isDirectory) {
                    zipPath += File.separator;
                }
                if (zippingDecider.shouldZip(zipPath)) {
                    if (isDirectory) {
                        zipRecursively(file, zipStream, thisDirZipPath);
                    } else {
                        writeEntry(file, zipStream, thisDirZipPath);
                    }
                }
            } finally {
            }
        }
    }
}
