package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public class ProjectFetcherTest {
    private ServerManager server;
    private ProjectDownloader fetcher;
    
    @Before
    public void setup() {
        server = mock(ServerManager.class);
        fetcher = new ProjectDownloader(server);
    }

    @Test
    public void attemptsToDownloadProjectFromCorrectUrl() {
        Exercise exercise = mock(Exercise.class);
        when(exercise.getDownloadUrl()).thenReturn("url");

        ZippedProject mockZip = new ZippedProject();
        when(server.getExerciseZip(any(String.class))).thenReturn(mockZip);

        ZippedProject retrievedZip = fetcher.downloadExercise(exercise);

        verify(server, times(1)).getExerciseZip("url");
        assertEquals(mockZip, retrievedZip);
    }
    
    @Test
    public void attemptsToDownloadFromAllUrlsWhenFetchingMultipleExercises() {
        Exercise exercise1 = mock(Exercise.class);
        Exercise exercise2 = mock(Exercise.class);
        Exercise exercise3 = mock(Exercise.class);
        when(exercise1.getDownloadUrl()).thenReturn("url1");
        when(exercise2.getDownloadUrl()).thenReturn("url2");
        when(exercise3.getDownloadUrl()).thenReturn("url3");
        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(exercise1);
        exercises.add(exercise2);
        exercises.add(exercise3);

        ZippedProject mockZip1 = new ZippedProject();
        ZippedProject mockZip2 = new ZippedProject();
        ZippedProject mockZip3 = new ZippedProject();
        when(server.getExerciseZip("url1")).thenReturn(mockZip1);
        when(server.getExerciseZip("url2")).thenReturn(mockZip2);
        when(server.getExerciseZip("url3")).thenReturn(mockZip3);

        List<ZippedProject> returnedList = fetcher.downloadExercises(exercises);

        verify(server, times(1)).getExerciseZip("url1");
        verify(server, times(1)).getExerciseZip("url2");
        verify(server, times(1)).getExerciseZip("url3");
        assertTrue(returnedList.contains(mockZip1));
        assertTrue(returnedList.contains(mockZip2));
        assertTrue(returnedList.contains(mockZip3));
    }
    

}
