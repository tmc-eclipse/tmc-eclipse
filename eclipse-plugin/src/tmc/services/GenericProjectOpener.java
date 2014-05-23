package tmc.services;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;

public class GenericProjectOpener {

    public GenericProjectOpener() {

    }

    public void openProjects(List<Exercise> list) {
        for (Exercise e : list) {
            try {
                new MavenProjectOpener(Core.getSettings().getExerciseFilePath() + "/"
                        + Core.getSettings().getCurrentCourseName() + parsePath(e.getName()) + "pom.xml")
                        .importAndOpen();
            } catch (CoreException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

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
