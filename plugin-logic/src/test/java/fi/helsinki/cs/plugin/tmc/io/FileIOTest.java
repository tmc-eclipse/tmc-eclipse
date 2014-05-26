package fi.helsinki.cs.plugin.tmc.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

public class FileIOTest {

    private IO io;

    @Before
    public void setUp() {
        io = new FileIO("invalid.file");
    }

    @Test
    public void testGetReaderReturnsNullIfFileDoesntExist() {
        assertNull(io.getReader());
    }
    
    @Test
    public void testFileExistsAndGetName(){
    	assertEquals(false, io.fileExists());
    	assertEquals(false, new FileIO("src/").fileExists());
    	assertEquals(true, new FileIO("pom.xml").fileExists());
    	assertEquals("pom.xml", new FileIO("pom.xml").getName());
    }
    
    @Test
    public void testGetreader() throws IOException{
    	FileIO fio = new FileIO("pom.xml");
    	Reader r = fio.getReader();
    	assertEquals((char) r.read(), '<');
    	assertEquals((char) r.read(), 'p');
    }

}
