package fi.helsinki.cs.plugin.tmc.spyware.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;
import fi.helsinki.cs.plugin.tmc.spyware.SnapshotInfo;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ActiveThreadSet;
import fi.helsinki.cs.plugin.tmc.storage.DataSource;

public class SnapshotTakerTest {
    private SnapshotTaker taker;
    private EventReceiver receiver;
    private DataSource<Project> source;
    private Settings settings;
    private LoggableEvent event;

    private String relPath;
    private String fullPath;

    @Before
    public void setUp() throws Exception {
        relPath = "src/test/java/fi/helsinki/cs/plugin/tmc/spyware/services/testProject/aaa.txt";
        fullPath = new File(relPath).getCanonicalPath();
        createReceiver();
        createSource();

        List<Project> sourceList = new ArrayList<Project>();
        List<String> projectFiles = new ArrayList<String>();

        projectFiles.add("src/test/java/fi/helsinki/cs/plugin/tmc/spyware/services/testProject/build.xml");
        sourceList.add(new Project(new Exercise("testProject", "course1"), projectFiles));
        source.save(sourceList);

        settings = mock(Settings.class);
        when(settings.isSpywareEnabled()).thenReturn(true);

        this.taker = new SnapshotTaker(new ActiveThreadSet(), receiver, settings, new ProjectDAO(source));
    }

    @Test
    public void receiverDoesNotReceiveEventIfSpywareIsDisabled() throws IOException, InterruptedException {
        when(settings.isSpywareEnabled()).thenReturn(false);

        taker.execute(new SnapshotInfo("testProject", "", relPath, "", fullPath, ChangeType.FILE_CHANGE));

        Thread.sleep(50);

        assertEquals(event, null);
    }

    @Test
    public void fileChangeTest() {
        SnapshotInfo info = new SnapshotInfo("testProject", "", relPath, "", fullPath, ChangeType.FILE_CHANGE);
        taker.execute(info);

        while (event == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        }

        assertEquals(event.getCourseName(), "course1");
    }

    @Test
    public void fileRenameTest() {
        SnapshotInfo info = new SnapshotInfo("testProject", relPath + "a", relPath, fullPath + "a", fullPath,
                ChangeType.FILE_RENAME);
        taker.execute(info);

        while (event == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        }

        assertEquals(event.getCourseName(), "course1");
    }

    private void createReceiver() {
        this.receiver = new EventReceiver() {

            @Override
            public void close() throws IOException {
            }

            @Override
            public void receiveEvent(LoggableEvent event) {
                SnapshotTakerTest.this.event = event;
            }
        };
    }

    private void createSource() {
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
