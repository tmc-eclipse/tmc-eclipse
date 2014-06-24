package fi.helsinki.cs.tmc.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.io.ProjectScanner;
import fi.helsinki.cs.tmc.core.storage.CourseStorage;
import fi.helsinki.cs.tmc.core.storage.DataSource;
import fi.helsinki.cs.tmc.core.storage.ProjectStorage;

/**
 * Class that initializes various daos
 * 
 */
public class DAOManager {

    public static final String DEFAULT_COURSES_PATH = "courses.tmp";
    public static final String DEFAULT_PROJECTS_PATH = "projects.tmp";

    private FileIO coursesPath;
    private FileIO projectsPath;

    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private ReviewDAO reviewDAO;

    public DAOManager() {
        this(new FileIO(DEFAULT_COURSES_PATH), new FileIO(DEFAULT_PROJECTS_PATH));
    }

    public DAOManager(FileIO coursesPath, FileIO projectsPath) {
        this.coursesPath = coursesPath;
        this.projectsPath = projectsPath;
    }

    public CourseDAO getCourseDAO() {
        if (this.courseDAO == null) {
            initialize();
        }

        return courseDAO;
    }

    public ProjectDAO getProjectDAO() {
        if (this.projectDAO == null) {
            initialize();
        }

        return projectDAO;
    }

    public ReviewDAO getReviewDAO() {
        if (this.reviewDAO == null) {
            initialize();
        }

        return reviewDAO;
    }

    private void initialize() {
        DataSource<Course> courseStorage = new CourseStorage(coursesPath);
        this.courseDAO = new CourseDAO(courseStorage);

        DataSource<Project> projectStorage = new ProjectStorage(projectsPath);
        this.projectDAO = new ProjectDAO(projectStorage);

        this.reviewDAO = new ReviewDAO();

        linkCoursesAndExercises();
        scanProjectFiles();
    }

    private void scanProjectFiles() {
        ProjectScanner projectScanner = new ProjectScanner(projectDAO);
        projectScanner.updateProjects();
        projectDAO.save();
    }

    private void linkCoursesAndExercises() {
        Map<Course, List<Exercise>> exercisesMap = new HashMap<Course, List<Exercise>>();

        for (Project project : projectDAO.getProjects()) {
            Exercise exercise = project.getExercise();
            Course course = courseDAO.getCourseByName(exercise.getCourseName());
            exercise.setCourse(course);
            exercise.setProject(project);

            if (!exercisesMap.containsKey(course)) {
                exercisesMap.put(course, new ArrayList<Exercise>());
            }
            exercisesMap.get(course).add(exercise);
        }

        for (Course course : courseDAO.getCourses()) {
            if (exercisesMap.containsKey(course)) {
                course.setExercises(exercisesMap.get(course));
            }
        }
    }

}
