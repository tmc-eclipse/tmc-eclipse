package fi.helsinki.cs.plugin.tmc.io;

import java.io.Reader;
import java.io.Writer;

public interface IO {

	public String getName();
	public String getPath();
	
	public boolean fileExists();
	public Writer getWriter();
	public Reader getReader();
	public abstract void write(byte[] bytes);
	public abstract void createFolderTree(boolean onlyParents);

}