package fi.helsinki.cs.tmc.core.spyware.utility;

import com.google.gson.JsonObject;

/**
 * A convenient way to build ad-hoc JSON objects.
 */
public class JsonMaker {
    public static JsonMaker create() {
        return new JsonMaker();
    }

    private JsonObject toplevel;

    public JsonMaker() {
        this(new JsonObject());
    }

    public JsonMaker(JsonObject toplevel) {
        this.toplevel = toplevel;
    }

    public JsonMaker add(String name, String value) {
        toplevel.addProperty(name, value);
        return this;
    }

    public JsonMaker add(String name, long value) {
        toplevel.addProperty(name, value);
        return this;
    }

    public JsonMaker add(String name, boolean value) {
        toplevel.addProperty(name, value);
        return this;
    }

    @Override
    public String toString() {
        return toplevel.toString();
    }
}