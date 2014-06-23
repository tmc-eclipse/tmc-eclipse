package fi.helsinki.cs.plugin.tmc.spyware.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.spyware.DocumentInfo;
import fi.helsinki.cs.plugin.tmc.spyware.async.DocumentSendThread;
import fi.helsinki.cs.plugin.tmc.spyware.utility.diff_match_patch;

public class DocumentSendThreadTest {
    private DocumentSendThread thread;
    private EventReceiver receiver;
    private DocumentInfo info;
    private Project project;
    private Map<String, String> cache;
    private diff_match_patch PATCH_GENERATOR;

    @Before
    public void setUp() throws Exception {
        this.receiver = mock(EventReceiver.class);
        this.info = new DocumentInfo("a", "a", "a", "a", 0, 1);
        this.project = mock(Project.class);
        this.cache = new HashMap<String, String>();
        this.PATCH_GENERATOR = mock(diff_match_patch.class);

        when(project.getExercise()).thenReturn(new Exercise("name1", "course1"));

        this.thread = new DocumentSendThread(receiver, info, project, cache, PATCH_GENERATOR);
    }

    @Test
    public void generatePatchesTest() {
        thread.run();

        assertEquals(cache.size(), 1);
    }

    @Test
    public void insertEventTest() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoggableEvent event = (LoggableEvent)invocation.getArguments()[0];
                assertEquals("text_insert", event.getEventType());
                return null;
            } }).when(receiver).receiveEvent(any(LoggableEvent.class));
            
        
        thread.run();
        verify(receiver, times(1)).receiveEvent(any(LoggableEvent.class));
    }
    
    @Test
    public void removeEventTest() {
        this.info = new DocumentInfo("", "", "", "", 0, 1);
        this.thread = new DocumentSendThread(receiver, info, project, cache, PATCH_GENERATOR);
        
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoggableEvent event = (LoggableEvent)invocation.getArguments()[0];
                assertEquals("text_remove", event.getEventType());
                return null;
            } }).when(receiver).receiveEvent(any(LoggableEvent.class));
            
        
        thread.run();
        verify(receiver, times(1)).receiveEvent(any(LoggableEvent.class));
    }
    
    @Test
    public void pasteEventTest() {
        if ("true".equals(System.getenv("TRAVIS"))) {
            return;
        }
        
        StringSelection s = new StringSelection("a a");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, s);
        
        this.info = new DocumentInfo("", "", "", "a a", 0, 3);
        this.thread = new DocumentSendThread(receiver, info, project, cache, PATCH_GENERATOR);
        
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoggableEvent event = (LoggableEvent)invocation.getArguments()[0];
                assertEquals("text_paste", event.getEventType());
                return null;
            } }).when(receiver).receiveEvent(any(LoggableEvent.class));
            
        
        thread.run();

        verify(receiver, times(1)).receiveEvent(any(LoggableEvent.class));
    }

}
