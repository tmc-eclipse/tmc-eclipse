package fi.helsinki.cs.plugin.tmc.io;

import java.io.Reader;
import java.io.Writer;

public interface IO {

	public boolean exists();
	public Writer getWriter();
	public Reader getReader();

}