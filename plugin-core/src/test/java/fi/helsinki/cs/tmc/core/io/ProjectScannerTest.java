package fi.helsinki.cs.tmc.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectStatus;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;

public class ProjectScannerTest {

    private ProjectScanner scanner;
    private ProjectDAO dao;
    private IO io;

    @Before
    public void setUp() {
        dao = mock(ProjectDAO.class);
        IOFactory factory = mock(IOFactory.class);
        io = mock(IO.class);
        when(factory.createIO(anyString())).thenReturn(io);
        scanner = new ProjectScanner(dao, factory);
    }

    @Test
    public void updateProjectHasNoInteractionsIfStatusIsDeleted() {
        Project project = mock(Project.class);
        when(project.getStatus()).thenReturn(ProjectStatus.DELETED);
        scanner.updateProject(project);
        verify(project, times(1)).getStatus();
        verifyNoMoreInteractions(dao);
        verifyNoMoreInteractions(project);
    }

    @Test
    public void correctFilesAreAddedToProject() {
        when(io.directoryExists()).thenReturn(true);
        when(io.getPath()).thenReturn("path_1");

        List<IO> list = new ArrayList<IO>();
        IO childIO = mock(IO.class);
        list.add(childIO);
        when(io.getChildren()).thenReturn(list);

        when(childIO.getPath()).thenReturn("path_2");
        when(childIO.getChildren()).thenReturn(new ArrayList<IO>());
        when(childIO.fileExists()).thenReturn(true);

        when(io.getChildren()).thenReturn(list);

        Project project = mock(Project.class);

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<String> paths = (List<String>) invocation.getArguments()[0];

                assertEquals(2, paths.size());
                assertTrue(paths.contains("path_1"));
                assertTrue(paths.contains("path_2"));

                return null;
            }

        }).when(project).setProjectFiles(anyListOf(String.class));

        scanner.updateProject(project);
        verify(project, times(1)).setProjectFiles(anyListOf(String.class));

    }

    @Test
    public void projectStatusIsSetAsNotDownloadedIfItDoesNotExistOnDisk() {
        Project project = mock(Project.class);
        when(project.existsOnDisk()).thenReturn(false);
        scanner.updateProject(project);
        verify(project, times(1)).setStatus(ProjectStatus.NOT_DOWNLOADED);
    }

    @Test
    public void projectStatusIsSetAsDownloadedIfItDoesExistOnDisk() {
        Project project = mock(Project.class);
        when(project.existsOnDisk()).thenReturn(true);
        scanner.updateProject(project);
        verify(project, times(1)).setStatus(ProjectStatus.DOWNLOADED);
    }

    @Test
    public void updateProjectIsCalledOnAllProjectsWhenCallingUpdateProjects() {
        List<Project> projects = new ArrayList<Project>();

        Project project = mock(Project.class);
        when(project.getStatus()).thenReturn(ProjectStatus.DELETED);
        projects.add(project);

        project = mock(Project.class);
        when(project.existsOnDisk()).thenReturn(false);
        projects.add(project);

        project = mock(Project.class);
        when(project.existsOnDisk()).thenReturn(true);
        projects.add(project);

        when(dao.getProjects()).thenReturn(projects);
        scanner.updateProjects();

        verify(projects.get(0), times(1)).getStatus();
        verifyNoMoreInteractions(projects.get(0));

        verify(projects.get(1), times(1)).getStatus();
        verify(projects.get(1), times(1)).setProjectFiles(anyListOf(String.class));
        verify(projects.get(1), times(1)).setStatus(ProjectStatus.NOT_DOWNLOADED);

        verify(projects.get(2), times(1)).getStatus();
        verify(projects.get(2), times(1)).setProjectFiles(anyListOf(String.class));
        verify(projects.get(2), times(1)).setStatus(ProjectStatus.DOWNLOADED);
    }
}
