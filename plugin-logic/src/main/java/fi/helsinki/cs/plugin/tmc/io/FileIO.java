package fi.helsinki.cs.plugin.tmc.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class FileIO implements IO {
	
	private File file;
	
	public FileIO(File file) {
		this.file = file;
	}
	
	@Override
	public boolean exists() {
		return file.exists() && !file.isDirectory();
	}
	
	@Override
	public Writer getWriter() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			return new OutputStreamWriter(new BufferedOutputStream(fos), "UTF-8");
		} catch(IOException e) {
			return null;
		}
	}
	
	@Override
	public Reader getReader() {
		try {
			FileInputStream fis = new FileInputStream(file);
			return new InputStreamReader(new BufferedInputStream(fis), "UTF-8");
		} catch(IOException e) {
			return null;
		}
	}
	
}
