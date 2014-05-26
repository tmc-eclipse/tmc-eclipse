package tmc.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.tasks.ProjectOpener;

public class GenericProjectOpener implements ProjectOpener {
    // TODO: projektien avaus.
    public GenericProjectOpener() {

    }

    public void open(Exercise e) {
        // try {
        // new MavenProjectOpener(Core.getSettings().getExerciseFilePath() + "/"
        // + Core.getSettings().getCurrentCourseName() + parsePath(e.getName())
        // + "pom.xml").importAndOpen();
        // } catch (CoreException | IOException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }

        // try {
        // new AntProjectOpener(Core.getSettings().getExerciseFilePath() + "/"
        // + Core.getSettings().getCurrentCourseName() + parsePath(e.getName())
        // + ".project").importAndOpen();
        // } catch (CoreException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }

        try {
            new CProjectOpener(Core.getSettings().getExerciseFilePath() + "/"
                    + Core.getSettings().getCurrentCourseName() + parsePath(e.getName()), e.getName()).importAndOpen();
        } catch (OperationCanceledException | URISyntaxException | CoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public String parsePath(String path) {
        String[] parts = path.split("-");
        String toReturn = "/";
        for (int i = 0; i < parts.length; i++) {
            toReturn += parts[i] + "/";
        }
        return toReturn;
    }

}
