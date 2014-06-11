package fi.helsinki.cs.plugin.tmc.spyware.services;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.BadLocationException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.spyware.utility.JsonMaker;
import fi.helsinki.cs.plugin.tmc.spyware.utility.diff_match_patch;
import fi.helsinki.cs.plugin.tmc.spyware.utility.diff_match_patch.Patch;

/**
 * This class handles text inserts, removals and cut\pastes. It uses information
 * it receives from the plugin
 * 
 */
public class DocumentChangeHandler {

    private static final Logger log = Logger.getLogger(DocumentChangeHandler.class.getName());
    private static final diff_match_patch PATCH_GENERATOR = new diff_match_patch();
    private EventReceiver receiver;
    private Map<String, String> documentCache;

    public DocumentChangeHandler(EventReceiver receiver) {
        this.receiver = receiver;
        this.documentCache = new HashMap<String, String>();
    }

    public void handleEvent(Object o) {
        if (!Core.getSettings().isSpywareEnabled()) {
            System.out.println("Spyware disabled, bailing out");
        }

        String tempPathReplaceWithCorrectOne = "";

        Project project = Core.getProjectDAO().getProjectByFile(tempPathReplaceWithCorrectOne);

        if (project == null) {
            System.out.println("Not a TMC project, bailing out!");
        }

        createAndSendPatch(null, project);
    }

    private void createAndSendPatch(Object o, Project project) {
        List<Patch> patches;

        String full_path_to_file;
        String file_contents;
        String relative_path;
        boolean patchContainsFullDocument = !documentCache.containsKey(full_path_to_file);

        try {
            // generatePatches will cache the current version for future
            // patches; if the document was not in the cache previously, the
            // patch will
            // contain the full document content
            patches = generatePatches(full_path_to_file, file_contents);
        } catch (BadLocationException exp) {
            log.log(Level.WARNING, "Unable to generate patches from {0}.", relative_path);
            return;
        }
        // Remove event is handled here as the getText-method can cause
        // an error as the document state is the state after the event. As
        // the offsets are from the actual event, they may reference content
        // that is no longer in the current document.
        if (e.getType().equals(EventType.REMOVE)) {
            sendEvent(project.getExercise(), "text_remove",
                    generatePatchDescription(relative_path, patches, patchContainsFullDocument));
            return;
        }

        String eventText;

        if (isPasteEvent(eventText)) {
            sendEvent(project.getExercise(), "text_paste",
                    generatePatchDescription(relative_path, patches, patchContainsFullDocument));
        } else if (e.getType() == EventType.INSERT) {
            sendEvent(project.getExercise(), "text_insert",
                    generatePatchDescription(relative_path, patches, patchContainsFullDocument));
        }

    }

    private void sendEvent(Exercise ex, String eventType, String text) {
        LoggableEvent event = new LoggableEvent(ex, eventType, text.getBytes(Charset.forName("UTF-8")));
        receiver.receiveEvent(event);
    }

    private String generatePatchDescription(String relativePath, List<Patch> patches, boolean patchContainsFullDocument) {
        return JsonMaker.create().add("file", relativePath).add("patches", PATCH_GENERATOR.patch_toText(patches))
                .add("full_document", patchContainsFullDocument).toString();
    }

    private boolean isPasteEvent(String text) {
        if (text.length() <= 2 || isWhiteSpace(text)) {
            // if a short text or whitespace is inserted,
            // we skip checking for paste
            return false;
        }

        try {
            String clipboardData = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
            return text.equals(clipboardData);
        } catch (Exception exp) {
        }

        return false;
    }

    private boolean isWhiteSpace(String text) {
        // If an insert is just whitespace, it's probably an autoindent

        for (int i = 0; i < text.length(); ++i) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    // currently, if a document is not existing, the patch will
    // contain the full file
    private List<Patch> generatePatches(String key, String text) throws BadLocationException {
        String previous = "";
        if (documentCache.containsKey(key)) {
            previous = documentCache.get(key);
        }

        documentCache.put(key, text);

        return PATCH_GENERATOR.patch_make(previous, text);
    }
}