package fi.helsinki.cs.tmc.core.storage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

public class ProjectStorageTest {

    private ProjectStorage storage;
    private FileIO io;

    @Before
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        io = mock(FileIO.class);
        storage = new ProjectStorage(io);

    }

    @Test
    public void loadReturnsEmptyListIfFileDoesNotExist() {
        when(io.fileExists()).thenReturn(false);
        assertEquals(0, storage.load().size());
    }

    @Test(expected = UserVisibleException.class)
    public void loadThrowsIfReaderIsNull() {
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(null);
        storage.load();
    }

    @Test
    public void loadDoesNotThrowIfReaderCloseThrows() throws IOException {

        when(io.fileExists()).thenReturn(true);
        Reader reader = mock(Reader.class);

        // return -1 to signify end of stream; otherwise gson deserialization
        // hangs while waiting for input
        when(reader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

        doThrow(new IOException("Foo")).when(reader).close();
        when(io.getReader()).thenReturn(reader);
        storage.load();
        verify(reader, times(1)).close();
    }

    @Test
    public void loadReturnsArrayWithCorrectSizeWithCorrectJSON() {
        Reader reader = createMockWithJson();
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(reader);
        List<Project> list = storage.load();
        assertEquals(1, list.size());
    }

    @Test
    public void loadReturnsArrayWithCorrectDataWithCorrectJSON() {
        Reader reader = createMockWithJson();
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(reader);

        verifyData();
    }

    @Test
    public void loadReturnsArrayWithCorrectSizeIfReadIsSuccessfulButCloseThrowsException() throws IOException {
        Reader reader = createMockWithJson();
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(reader);
        doThrow(new IOException("Foo")).when(reader).close();

        List<Project> list = storage.load();
        assertEquals(1, list.size());
    }

    @Test
    public void loadReturnsArrayWithCorrectDataWithCorrectJSONIfCloseThrowsException() throws IOException {
        Reader reader = createMockWithJson();
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(reader);
        doThrow(new IOException("Foo")).when(reader).close();
        verifyData();
    }

    @Test
    public void loadReturnsEmptyListIfReadingEmptyFile() throws IOException {
        Reader reader = mock(Reader.class);
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(reader);
        when(reader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);
        assertEquals(0, storage.load().size());
    }

    @Test
    public void loadReturnsEmptyListIfLoadThrowsAndReadingEmptyFile() throws IOException {
        Reader reader = mock(Reader.class);
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(reader);
        when(reader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);
        doThrow(new IOException("Foo")).when(reader).close();
        assertEquals(0, storage.load().size());
    }

    @Test(expected = UserVisibleException.class)
    public void loadThrowsUserVisibleExceptionIfReadFails() throws IOException {
        Reader reader = mock(Reader.class);
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(reader);
        when(reader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("Foo"));
        storage.load();
    }

    private void verifyData() {
        List<Project> list = storage.load();
        Exercise e = list.get(0).getExercise();
        Project p = list.get(0);
        assertEquals(843, e.getId());
        assertEquals("name", e.getName());
        assertEquals("course_name", e.getCourseName());
        assertEquals("course_name", e.getCourseName());
        assertEquals("http://solution_url.com", e.getSolutionDownloadUrl());
        assertEquals("/path/to", p.getRootPath());
    }

    private Reader createMockWithJson() {
        Reader reader = mock(Reader.class);
        try {
            when(reader.read(any(char[].class), anyInt(), anyInt())).thenAnswer(new Answer<Integer>() {
                private String json = "{\"projects\": [{\"exercise\": {\"id\": 843,\"name\": \"name\","
                        + "\"courseName\": \"course_name\", \"deadlineDate\": null, \"deadline\": null,"
                        + "\"zip_url\": \"https://zip_url.com\","
                        + "\"solution_zip_url\": \"http://solution_url.com\","
                        + "\"return_url\": \"https://return_url.com\","
                        + "\"locked\": false, \"deadline_description\": null, \"returnable\": true,"
                        + "\"requires_review\": false, \"attempted\": true, \"completed\": true,"
                        + "\"reviewed\": true, \"all_review_points_given\": true,"
                        + "\"oldChecksum\": \"bb0571149a58adf71f0f2980fb2980d3\", \"updateAvailable\": false,"
                        + "\"checksum\": \"bb0571149a58adf71f0f2980fb2980d3\", \"memory_limit\": null" + "},"
                        + "\"projectFiles\": [\"/path/to/file\"], \"extraStudentFiles\": [],"
                        + "\"rootPath\": \"/path/to\", \"status\": \"DOWNLOADED\"}]}";
                private int position = 0;

                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    char[] buffer = (char[]) invocation.getArguments()[0];
                    int offset = (int) (invocation.getArguments()[1]);
                    int length = (int) (invocation.getArguments()[2]);

                    if (position >= json.length()) {
                        return -1;
                    }

                    int readCharacters = 0;
                    for (; readCharacters < length && position < json.length()
                            && readCharacters + offset < buffer.length; ++position, ++readCharacters) {
                        buffer[readCharacters + offset] = json.charAt(position);
                    }
                    position += readCharacters;

                    return readCharacters;
                }
            });
        } catch (IOException e) {
        }

        return reader;
    }
}
