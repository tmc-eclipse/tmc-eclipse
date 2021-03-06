package fi.helsinki.cs.tmc.core.async.tasks;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fi.helsinki.cs.tmc.core.async.TaskStatusMonitor;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectStatus;
import fi.helsinki.cs.tmc.core.domain.ProjectType;
import fi.helsinki.cs.tmc.core.domain.ZippedProject;
import fi.helsinki.cs.tmc.core.io.FakeIOFactory;
import fi.helsinki.cs.tmc.core.io.IOFactory;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectDownloader;
import fi.helsinki.cs.tmc.core.services.ProjectOpener;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class DownloaderTaskTest {

    private DownloaderTask task;
    private ProjectDownloader downloader;
    private ProjectOpener opener;
    private List<Exercise> exercises;
    private ProjectDAO projectDao;
    private Settings settings;
    private IdeUIInvoker invoker;
    private IOFactory io;

    private ZippedProject zipProject;
    TaskStatusMonitor progress;

    @Before
    public void setUp() {
        io = new FakeIOFactory();
        progress = mock(TaskStatusMonitor.class);

        downloader = mock(ProjectDownloader.class);

        zipProject = mock(ZippedProject.class);
        when(zipProject.getBytes()).thenReturn(new byte[0]);

        opener = mock(ProjectOpener.class);
        exercises = new ArrayList<Exercise>();
        exercises.add(mock(Exercise.class));
        projectDao = mock(ProjectDAO.class);

        settings = mock(Settings.class);
        when(settings.getExerciseFilePath()).thenReturn("foo/bar");
        when(settings.getCurrentCourseName()).thenReturn("testcourse");

        invoker = mock(IdeUIInvoker.class);

        when(downloader.downloadExercise(exercises.get(0))).thenReturn(zipProject);

        task = new DownloaderTask(downloader, opener, exercises, projectDao, settings, invoker, io);
    }

    @Test
    public void exerciseIsDownloaded() {
        task.start(progress);
        verify(downloader, times(1)).downloadExercise(exercises.get(0));
    }

    @Test
    public void getProjectByExerciseIsCalled() {
        task.start(progress);
        verify(projectDao, times(1)).getProjectByExercise(exercises.get(0));
    }

    @Test
    public void projectIsAddedToProjectDaoWhenNoProjectExistsFirst() {
        task.start(progress);
        verify(projectDao, times(1)).addProject(Mockito.any(Project.class));
    }

    @Test
    public void projectFilesAreSetWhenProjectExists() {

        Project project = mock(Project.class);
        when(project.getProjectType()).thenReturn(ProjectType.JAVA_ANT);
        when(project.getRootPath()).thenReturn("foo/bar");
        when(projectDao.getProjectByExercise(exercises.get(0))).thenReturn(project);

        task.start(progress);
        verify(project, times(1)).setProjectFiles(anyListOf(String.class));

    }

    @Test
    public void openerIsCalledWithCorrectParameter() {
        task.start(progress);
        verify(opener, times(1)).open(exercises.get(0));
    }

    @Test
    public void exerciseIsSetAsUpdated() {
        task.start(progress);
        verify(exercises.get(0), times(1)).setUpdateAvailable(false);
    }

    @Test
    public void projectStatusIsSetAsDownloaded() {

        Project project = mock(Project.class);
        when(project.getProjectType()).thenReturn(ProjectType.JAVA_ANT);
        when(project.getRootPath()).thenReturn("foo/bar");
        when(projectDao.getProjectByExercise(exercises.get(0))).thenReturn(project);

        task.start(progress);
        verify(project, times(1)).setStatus(ProjectStatus.DOWNLOADED);

    }

}
