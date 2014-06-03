package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class ExerciseFetcher {

	private ServerManager server;
	private CourseDAO courseDAO;
	private Course course;

	public ExerciseFetcher(ServerManager server, CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
		this.server = server;
	}

	public void updateExercisesForCurrentCourse() {
		try {
			this.course = courseDAO.getCourseByName(Core.getSettings()
					.getCurrentCourseName());
			List<Exercise> exercises;
			if (this.course != null) {
				exercises = server.getExercises("" + course.getId());
			} else {
				exercises = new ArrayList<Exercise>();
			}
			if (exercises != null) {
				course.setExercises(exercises);
			}
		} catch (UserVisibleException e) {
			Core.getErrorHandler().handleException(e);
		} catch (NullPointerException n) {
			Core.getErrorHandler()
					.handleException(
							new NullPointerException(
									"Remember to select your course from TMC -> Settings"));
		}
	}

	public Exercise getExerciseByName(String name) {
		for (Exercise e : course.getExercises()) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public List<Exercise> getExercisesForCurrentCourse() {
		if (course != null) {
			return course.getExercises();
		} else {
			return new ArrayList<Exercise>();
		}
	}

	public String[] getCurrentCoursesExerciseNames() {
		List<Exercise> exerciseNames = course.getExercises();
		String[] names = new String[exerciseNames.size()];

		for (int i = 0; i < exerciseNames.size(); i++) {
			names[i] = exerciseNames.get(i).getName();
		}

		return names;
	}
}
