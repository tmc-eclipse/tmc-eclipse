package fi.helsinki.cs.plugin.tmc.spyware;

public class DocumentInfo {
    private String fullPath;
    private String relativePath;
    private String editorText;
    private String eventText;
    private int offset;
    private int length;

    /**
     * Helper class that carries needed info for logging text change events. If
     * editorText is empty (whitespace is not empty!), it is considered to be an
     * deletion event
     */
    public DocumentInfo(String fullPath, String relativePath, String editorText, String eventText, int offset,
            int length) {
        this.editorText = editorText;
        this.eventText = eventText;
        this.fullPath = fullPath;
        this.relativePath = relativePath;
        this.offset = offset;
        this.length = length;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getEditorText() {
        return editorText;
    }

    public String getEventText() {
        return eventText;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

}
