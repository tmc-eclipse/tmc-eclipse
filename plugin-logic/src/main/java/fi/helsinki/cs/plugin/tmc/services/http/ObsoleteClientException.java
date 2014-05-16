package fi.helsinki.cs.plugin.tmc.services.http;

import fi.helsinki.cs.plugin.tmc.services.web.UserVisibleException;

public class ObsoleteClientException extends UserVisibleException {
    public ObsoleteClientException() {
        super("Please update the TMC plugin.\nUse Help -> Check for Updates.");
    }
}