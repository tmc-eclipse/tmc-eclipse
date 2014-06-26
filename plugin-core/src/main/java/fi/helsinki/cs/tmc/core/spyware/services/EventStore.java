package fi.helsinki.cs.tmc.core.spyware.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.spyware.utility.ByteArrayGsonSerializer;

public class EventStore {
    private static final Logger log = Logger.getLogger(EventStore.class.getName());

    private FileIO configFile;

    public EventStore(FileIO configFile) {
        this.configFile = configFile;
    }

    public void save(LoggableEvent[] events) throws IOException {
        String text = getGson().toJson(events);
        Writer writer = configFile.getWriter();
        try {
            writer.write(text);
        } finally {
            writer.close();
        }
    }

    public LoggableEvent[] load() throws IOException {
        StringWriter writer = new StringWriter();
        Reader reader = configFile.getReader();
        LoggableEvent[] result = new LoggableEvent[0];

        if (reader == null) {
            return result;
        }
        try {
            IOUtils.copy(reader, writer);
            result = getGson().fromJson(writer.toString(), LoggableEvent[].class);
            if (result == null) {
                return new LoggableEvent[0];
            }
        } finally {
            writer.close();
            reader.close();
        }

        return result;
    }

    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(byte[].class, new ByteArrayGsonSerializer()).create();
    }

    public void clear() throws IOException {
        Writer writer = configFile.getWriter();
        try {
            writer.write("");
        } finally {
            writer.close();
        }
    }
}