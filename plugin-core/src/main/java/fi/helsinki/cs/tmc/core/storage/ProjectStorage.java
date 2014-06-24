package fi.helsinki.cs.tmc.core.storage;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.storage.formats.ProjectsFileFormat;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

public class ProjectStorage implements DataSource<Project> {

    private FileIO io;
    private Gson gson;

    public ProjectStorage(FileIO io) {
        this.io = io;
        this.gson = createGson();
    }

    @Override
    public List<Project> load() {
        if (!io.fileExists()) {
            return new ArrayList<Project>();
        }
        ProjectsFileFormat projectList = null;
        Reader reader = io.getReader();
        if (reader == null) {
            throw new UserVisibleException("Could not load project data from local storage.");
        }
        try {
            projectList = gson.fromJson(reader, ProjectsFileFormat.class);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO: Log here?
                return projectList.getProjects();
            }
        }
        return projectList.getProjects();
    }

    @Override
    public void save(List<Project> projects) {
        ProjectsFileFormat projectList = new ProjectsFileFormat();
        projectList.setProjects(projects);

        if (io == null) {
            throw new UserVisibleException("Could not save project data to local storage.");
        }

        Writer writer = io.getWriter();
        if (writer == null) {
            throw new UserVisibleException("Could not save project data to local storage.");
        }

        gson.toJson(projectList, writer);
        try {
            writer.close();
        } catch (IOException e) {
            // TODO: Log here?
            return;
        }
    }

    private Gson createGson() {
        return new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

}
