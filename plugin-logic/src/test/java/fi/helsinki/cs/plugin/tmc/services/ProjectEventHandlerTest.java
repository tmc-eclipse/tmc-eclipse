package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ProjectStatus;
import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;
import fi.helsinki.cs.plugin.tmc.spyware.SnapshotInfo;

public class ProjectEventHandlerTest {

    private Project project;
    private ProjectDAO projectDAO;

    private ProjectEventHandler handler;

    @Before
    public void setUp() {
        projectDAO = mock(ProjectDAO.class);
        project = mock(Project.class);

        ArrayList<String> testExercise1Files = new ArrayList<String>();
        testExercise1Files.add("testCourse/testExercise1/existingFile");

        when(project.getReadOnlyProjectFiles()).thenReturn(testExercise1Files);

        when(projectDAO.getProjectByFile("testCourse/testExercise1/existingFile")).thenReturn(project);
        when(projectDAO.getProjectByFile("testCourse/testExercise1/newFile")).thenReturn(project);

        when(project.containsFile("testCourse/testExercise1/nonExistingFile")).thenReturn(false);
        when(project.containsFile("testCourse/testExercise1/existingFile")).thenReturn(true);

        when(project.getRootPath()).thenReturn("testCourse/testExercise1");

        handler = new ProjectEventHandler(projectDAO);
    }

    @Test
    public void testHandleSnapshotRenameWithInvalidFile() {
        SnapshotInfo snapshot = new SnapshotInfo("project", "", "", "testCourse/testExercise1/nonExistingFile", "",
                ChangeType.FILE_RENAME);

        handler.handleSnapshot(snapshot);

        verify(projectDAO, times(1)).getProjectByFile("testCourse/testExercise1/nonExistingFile");
        verify(projectDAO, times(1)).getProjects();

        verify(project, never()).getReadOnlyProjectFiles();
        verify(project, never()).setProjectFiles(any(List.class));
    }

    @Test
    public void testHandleDeletionWithValidProject() {

        Mockito.doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<String> param = (List<String>) invocation.getArguments()[0];
                assertEquals(0, param.size());
                return null;
            }

        }).when(project).setProjectFiles(Mockito.anyListOf(String.class));
        String filePath = "testCourse/testExercise1";
        when(projectDAO.getProjectByFile(filePath)).thenReturn(project);
        handler.handleDeletion(filePath);
        verify(project, times(1)).setStatus(ProjectStatus.DELETED);

    }

    @Test
    public void testHandleCreatingFile() {
        // SnapshotInfo snapshot = new SnapshotInfo("project", "", "", "",
        // "testCourse/testExercise1/newFile",
        // ChangeType.FILE_CREATE);
        //
        // when(project.getProjectFiles()).thenReturn(new ArrayList<String>());
        //
        // handler.handle(snapshot);
        //
        // verify(projectDAO,
        // times(1)).getProjectByFile("testCourse/testExercise1/newFile");
        // verify(projectDAO, times(1)).getProjects();
        //
        // verify(project, times(1)).getProjectFiles();
        //
        // List<String> listWithNewFiles = new ArrayList<String>();
        // listWithNewFiles.add("testCourse/testExercise1/newFile");
        //
        // verify(project, times(1)).setProjectFiles(listWithNewFiles);
    }
}
