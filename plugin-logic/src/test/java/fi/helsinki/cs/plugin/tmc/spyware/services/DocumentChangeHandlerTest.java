package fi.helsinki.cs.plugin.tmc.spyware.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.spyware.DocumentInfo;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ActiveThreadSet;
import fi.helsinki.cs.plugin.tmc.storage.DataSource;

public class DocumentChangeHandlerTest {
    private DocumentChangeHandler handler;
    private EventReceiver receiver;
    private Settings settings;
    private ProjectDAO dao;
    private DataSource<Project> source;
    private LoggableEvent event;

    private String relPath;
    private String fullPath;

    @Before
    public void setUp() throws Exception {
        relPath = "src/test/java/fi/helsinki/cs/plugin/tmc/spyware/services/testProject/aaa.txt";
        fullPath = new File(relPath).getCanonicalPath();

        this.settings = mock(Settings.class);
        this.event = null;

        createDataSource();
        createReceiver();

        List<String> projectFiles = new ArrayList<String>();
        projectFiles.add("build.xml");

        Project p = new Project(new Exercise("name1", "courseName1"), projectFiles);
        List<Project> l = new ArrayList<Project>();
        l.add(p);
        source.save(l);

        this.dao = new ProjectDAO(source);

        this.handler = new DocumentChangeHandler(receiver, new ActiveThreadSet(), settings, dao);

        when(settings.isSpywareEnabled()).thenReturn(true);
    }

    @Test
    public void receiverDoesNotReceiveEventIfSpywareIsDisabled() throws IOException, InterruptedException {
        when(settings.isSpywareEnabled()).thenReturn(false);

        handler.handleEvent(new DocumentInfo(fullPath, relPath, "", "", 0, 3));

        Thread.sleep(50);

        assertEquals(event, null);
    }

    @Test
    public void receiverDoesNotReceiveEventIfProjectIsNull() throws IOException, InterruptedException {
        ProjectDAO dao = mock(ProjectDAO.class);
        when(dao.getProjectByFile(any(String.class))).thenReturn(null);
        this.handler = new DocumentChangeHandler(receiver, new ActiveThreadSet(), settings, dao);

        handler.handleEvent(new DocumentInfo(fullPath, relPath, "", "", 0, 3));

        Thread.sleep(50);

        assertEquals(event, null);
    }

    @Test
    public void textRemoveEvent() {
        DocumentInfo info = new DocumentInfo(fullPath, relPath, "", "", 0, 3);
        handler.handleEvent(info);

        int i = 0;
        while (event == null) {
            if (i > 400) {
                fail("LoggableEvent is null after waiting over 2000ms");
            }
            i++;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(event.getEventType(), "text_remove");
        assertEquals(event.getCourseName(), "courseName1");
    }

    @Test
    public void textPasteEvent() {
        if ("true".equals(System.getenv("TRAVIS"))) {
            return;
        }
        
        StringSelection s = new StringSelection("aaa");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, s);
        DocumentInfo info = new DocumentInfo(fullPath, relPath, "", "aaa", 0, 3);
        handler.handleEvent(info);

        int i = 0;
        while (event == null) {
            if (i > 400) {
                fail("LoggableEvent is null after waiting over 2000ms");
            }
            i++;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertEquals(event.getEventType(), "text_paste");
    }

    @Test
    public void textPasteEventWithSomeWhitespaces() {
        if ("true".equals(System.getenv("TRAVIS"))) {
            return;
        }
        
        StringSelection s = new StringSelection("  aa  a ");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, s);
        DocumentInfo info = new DocumentInfo(fullPath, relPath, "", "  aa  a ", 0, 3);
        handler.handleEvent(info);

        int i = 0;
        while (event == null) {
            if (i > 400) {
                fail("LoggableEvent is null after waiting over 2000ms");
            }
            i++;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertEquals(event.getEventType(), "text_paste");
    }

    public void textInsertEvent() {
        DocumentInfo info = new DocumentInfo(fullPath, relPath, "", "aa", 0, 3);
        handler.handleEvent(info);

        int i = 0;
        while (event == null) {
            if (i > 400) {
                fail("LoggableEvent is null after waiting over 2000ms");
            }
            i++;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(event.getEventType(), "text_insert");
    }

    @Test
    public void textInsertEventWithThreeWhitespaces() {
        DocumentInfo info = new DocumentInfo(fullPath, relPath, "", "    ", 0, 3);
        handler.handleEvent(info);

        int i = 0;
        while (event == null) {
            if (i > 400) {
                fail("LoggableEvent is null after waiting over 2000ms");
            }
            i++;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(event.getEventType(), "text_insert");
    }

    @Test
    public void textInsertEventWithOneWhitespace() {
        DocumentInfo info = new DocumentInfo(fullPath, relPath, "", " ", 0, 3);
        handler.handleEvent(info);

        int i = 0;
        while (event == null) {
            if (i > 400) {
                fail("LoggableEvent is null after waiting over 2000ms");
            }
            i++;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(event.getEventType(), "text_insert");
    }

    private void createReceiver() {
        this.receiver = new EventReceiver() {

            @Override
            public void close() throws IOException {
            }

            @Override
            public void receiveEvent(LoggableEvent event) {
                DocumentChangeHandlerTest.this.event = event;
            }
        };

    }

    private void createDataSource() {
        this.source = new DataSource<Project>() {
            private List<Project> list = new ArrayList<Project>();

            @Override
            public void save(List<Project> elements) {
                list = elements;
            }

            @Override
            public List<Project> load() {
                return list;
            }
        };
    }
}
