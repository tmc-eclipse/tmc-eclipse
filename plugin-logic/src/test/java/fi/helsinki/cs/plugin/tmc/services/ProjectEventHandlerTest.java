package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ProjectStatus;
import fi.helsinki.cs.plugin.tmc.io.ProjectScanner;
import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;
import fi.helsinki.cs.plugin.tmc.spyware.SnapshotInfo;
import static org.mockito.Mockito.*;

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
        handler.handleDeletion("testCourse/testExercise1");
        assert (project.getStatus() == ProjectStatus.DELETED);
        assert (project.getReadOnlyProjectFiles().size() == 0);
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
