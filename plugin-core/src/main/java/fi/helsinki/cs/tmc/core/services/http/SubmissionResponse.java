package fi.helsinki.cs.tmc.core.services.http;

import java.net.URI;

public class SubmissionResponse {
    public final URI submissionUrl;
    public final URI pasteUrl;

    public SubmissionResponse(URI submissionUrl, URI pasteUrl) {
        this.submissionUrl = submissionUrl;
        this.pasteUrl = pasteUrl;
    }
}
