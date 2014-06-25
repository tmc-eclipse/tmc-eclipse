package fi.helsinki.cs.tmc.core.domain;

/**
 * An enumeration class representing the status of a local TestMyCode project.
 */
public enum ProjectStatus {

    /**
     * Represents a project that has not yet been downloaded.
     */
    NOT_DOWNLOADED,

    /**
     * Represents a project which has been downloaded and is currently open in
     * the IDE workspace.
     */
    DOWNLOADED,

    /**
     * Represents a project which has been deleted. This means that the project
     * is no longer open in the IDE workspace and no changes to the project are
     * tracked by the TMC plugin. Local files of the project may or may not
     * still exist.
     */
    DELETED
}