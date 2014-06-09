package fi.helsinki.cs.plugin.tmc.spyware.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ByteArrayGsonSerializer;

public class EventStore {
    private static final Logger log = Logger.getLogger(EventStore.class.getName());

    private IO configFile;

    public EventStore(IO configFile) {
        this.configFile = configFile;
    }

    public void save(LoggableEvent[] events) throws IOException {
        String text = getGson().toJson(events);
        Writer writer = configFile.getWriter();
        writer.write(text);
        writer.close();

    }

    public LoggableEvent[] load() throws IOException {
        StringWriter writer = new StringWriter();
        Reader reader = configFile.getReader();
        if (reader == null) {
            return new LoggableEvent[0];
        }
        IOUtils.copy(reader, writer);

        LoggableEvent[] result = getGson().fromJson(writer.toString(), LoggableEvent[].class);
        writer.close();
        reader.close();
        if (result == null) {
            result = new LoggableEvent[0];
        }
        return result;
    }

    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(byte[].class, new ByteArrayGsonSerializer()).create();
    }

    public void clear() throws IOException {
        configFile.getWriter().write("");
    }
}