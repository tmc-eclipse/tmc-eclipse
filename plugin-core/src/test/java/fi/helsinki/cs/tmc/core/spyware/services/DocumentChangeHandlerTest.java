package fi.helsinki.cs.tmc.core.spyware.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.spyware.DocumentInfo;
import fi.helsinki.cs.tmc.core.spyware.services.DocumentChangeHandler;
import fi.helsinki.cs.tmc.core.spyware.services.EventReceiver;
import fi.helsinki.cs.tmc.core.spyware.utility.ActiveThreadSet;

public class DocumentChangeHandlerTest {
    private DocumentChangeHandler handler;
    private Settings settings;
    private ProjectDAO projectDAO;
    private ActiveThreadSet set;
    private EventReceiver receiver;

    @Before
    public void setUp() {
        this.settings = mock(Settings.class);
        this.projectDAO = mock(ProjectDAO.class);
        this.set = mock(ActiveThreadSet.class);
        this.receiver = mock(EventReceiver.class);

        when(settings.isSpywareEnabled()).thenReturn(true);

        this.handler = new DocumentChangeHandler(receiver, set, settings, projectDAO);
    }

    @Test
    public void handleEventIfSywareIsDisabled() {
        when(settings.isSpywareEnabled()).thenReturn(false);
        this.handler.handleEvent(new DocumentInfo("", "", "", "", 0, 1));

        verify(projectDAO, times(0)).getProjectByFile(any(String.class));
    }

    @Test
    public void handleEventIfProjectIsNull() {
        when(projectDAO.getProjectByFile(any(String.class))).thenReturn(null);
        this.handler.handleEvent(new DocumentInfo("", "", "", "", 0, 1));

        verify(set, times(0)).addThread(any(Thread.class));
    }

    @Test
    public void handleEventTest() {
        when(projectDAO.getProjectByFile(any(String.class))).thenReturn(new Project(new Exercise("name1", "course1")));
        this.handler.handleEvent(new DocumentInfo("", "", "", "", 0, 1));
        verify(set, times(1)).addThread(any(Thread.class));
    }

}
