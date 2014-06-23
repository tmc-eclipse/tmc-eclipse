package fi.helsinki.cs.plugin.tmc.spyware.async;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.BadLocationException;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.spyware.DocumentInfo;
import fi.helsinki.cs.plugin.tmc.spyware.services.EventReceiver;
import fi.helsinki.cs.plugin.tmc.spyware.services.LoggableEvent;
import fi.helsinki.cs.plugin.tmc.spyware.utility.JsonMaker;
import fi.helsinki.cs.plugin.tmc.spyware.utility.diff_match_patch;
import fi.helsinki.cs.plugin.tmc.spyware.utility.diff_match_patch.Patch;

public class DocumentSendThread extends Thread {
    private static final Logger log = Logger.getLogger(DocumentSendThread.class.getName());
    private final EventReceiver receiver;
    private final DocumentInfo info;
    private final Project project;
    private diff_match_patch PATCH_GENERATOR;
    private Map<String, String> documentCache;

    public DocumentSendThread(EventReceiver receiver, DocumentInfo info, Project project, Map<String, String> cache,
            diff_match_patch PATCH_GENERATOR) {
        super("Document change thread");
        this.receiver = receiver;
        this.info = info;
        this.project = project;
        this.documentCache = cache;
        this.PATCH_GENERATOR = PATCH_GENERATOR;
    }

    @Override
    public void run() {
        createAndSendPatch();
    }

    private void createAndSendPatch() {
        List<Patch> patches;
        boolean patchContainsFullDocument = !documentCache.containsKey(info.getFullPath());

        try {
            // generatePatches will cache the current version for future
            // patches; if the document was not in the cache previously, the
            // patch will
            // contain the full document content
            patches = generatePatches(info.getFullPath(), info.getEditorText());

        } catch (BadLocationException exp) {
            log.log(Level.WARNING, "Unable to generate patches from {0}.", info.getRelativePath());
            return;
        }

        // whitespace is still considered to be text; only truly empty text
        // is
        // considered to be deletion
        String text = generatePatchDescription(info.getRelativePath(), patches, patchContainsFullDocument);

        if (info.getEventText().length() == 0) {
            sendEvent(project.getExercise(), "text_remove", text);
            return;
        }

        if (isPasteEvent(info.getEventText())) {
            sendEvent(project.getExercise(), "text_paste", text);
        } else {
            sendEvent(project.getExercise(), "text_insert", text);
        }

    }

    private String generatePatchDescription(String relativePath, List<Patch> patches, boolean patchContainsFullDocument) {
        return JsonMaker.create().add("file", relativePath).add("patches", PATCH_GENERATOR.patch_toText(patches))
                .add("full_document", patchContainsFullDocument).toString();
    }

    private boolean isPasteEvent(String text) {
        if (text.trim().length() <= 2) {
            // if a short text or whitespace is inserted,
            // we skip checking for paste
            return false;
        }

        try {
            String clipboardData = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);

            // at least eclipse adds indentation whitespace to the beginning
            // of the text even if it's pasted, hence the trim
            return text.trim().equals(clipboardData.trim());
        } catch (Exception exp) {
        }

        return false;
    }

    // currently, if a document is not existing, the patch will
    // contain the full file
    private List<Patch> generatePatches(String key, String text) throws BadLocationException {
        String previous = "";
        synchronized (documentCache) {
            if (documentCache.containsKey(key)) {
                previous = documentCache.get(key);
            }

            documentCache.put(key, text);
        }
        return PATCH_GENERATOR.patch_make(previous, text);
    }

    private void sendEvent(Exercise ex, String eventType, String text) {
        LoggableEvent event = new LoggableEvent(ex, eventType, text.getBytes(Charset.forName("UTF-8")));
        receiver.receiveEvent(event);

    }
}