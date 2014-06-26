package fi.helsinki.cs.tmc.core.async.tasks;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FakeFileIO;
import fi.helsinki.cs.tmc.core.io.FakeIOFactory;
import fi.helsinki.cs.tmc.core.services.ProjectOpener;

public class OpenAllDownloadedExercisesTaskTest {

    private OpenAllDownloadedExercisesTask task;
    private List<Exercise> exercises;
    private Exercise e1;
    private Exercise e2;
    private Exercise e3;
    private Project p1;
    private Project p2;
    private Project p3;
    private ProjectOpener opener;

    private FakeIOFactory io;

    @Before
    public void setUp() throws Exception {
        exercises = new ArrayList<Exercise>();
        e1 = new Exercise();
        p1 = new Project(e1);
        e1.setProject(p1);
        exercises.add(e1);

        e2 = new Exercise();
        p2 = new Project(e2);
        e2.setProject(p2);
        exercises.add(e2);

        e3 = new Exercise();
        p3 = new Project(e3);
        e3.setProject(p3);
        exercises.add(e3);

        opener = mock(ProjectOpener.class);

        io = new FakeIOFactory();
        task = new OpenAllDownloadedExercisesTask("desc", exercises, opener, io);
    }

    @Test
    public void opensProjectsWithKnownBuildfile() throws IOException {
        FakeFileIO pom = io.getFake("/project/pom.xml");

        List<String> p1Files = new ArrayList<String>();
        p1Files.add(pom.getPath());
        p1.setProjectFiles(p1Files);

        task.run(e1);

        verify(opener, times(1)).open(any(Exercise.class));
    }

    @Test
    public void doesNotOpenProjectsWithoutKnownbuildfiles() {
        task.run(e1);
        verify(opener, times(0)).open(e1);
    }

    @Test
    public void doesNotOpenProjectswithfilesButWithoutKnownbuildfiles() {
        p1.addProjectFile("a");
        task.run(e1);
        verify(opener, times(0)).open(e1);
    }
}
