package fi.helsinki.cs.plugin.tmc.services.web;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.services.http.FailedHttpResponseException;
import fi.helsinki.cs.plugin.tmc.services.http.HttpRequestExecutor;
import fi.helsinki.cs.plugin.tmc.services.http.ServerAccess;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class JsonGetter {

    public String getJson(String url) {
        ServerAccess request = new ServerAccess();
        HttpRequestExecutor exec = request.createHttpTasks().createExecutor(request.getUrl(url));

        try {
            return IOUtils.toString(exec.call().getContent());
        } catch (FailedHttpResponseException httpe) {
            // Please refactor me.
            switch (httpe.getStatusCode()) {
            case HttpStatus.SC_FORBIDDEN:
                Core.getErrorHandler().handleException(
                        new UserVisibleException("Authentication failed, check your username and password"));
                break;
            case HttpStatus.SC_NOT_FOUND:
                String serverUrl = Core.getSettings().getServerBaseUrl();
                if (!serverUrl.isEmpty()) {
                    Core.getErrorHandler().handleException(
                            new UserVisibleException("Could not find TMC server at " + serverUrl));
                }
                break;
            default:
                Core.getErrorHandler().handleException(
                        new UserVisibleException("Something went wrong, please contact your TMC instructor"));
                break;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}