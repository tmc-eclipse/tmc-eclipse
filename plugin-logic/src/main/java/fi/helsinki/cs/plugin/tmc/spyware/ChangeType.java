package fi.helsinki.cs.plugin.tmc.spyware;

public enum ChangeType {
    NONE, FILE_CREATE, FOLDER_CREATE, FILE_CHANGE, FOLDER_CHANGE, 
    FILE_DELETE, FOLDER_DELETE, FILE_RENAME, FOLDER_RENAME;
}