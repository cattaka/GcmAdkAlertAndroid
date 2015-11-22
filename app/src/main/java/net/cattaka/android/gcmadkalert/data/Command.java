package net.cattaka.android.gcmadkalert.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by cattaka on 15/11/14.
 */
public enum Command {
    UNKNOWN("unknown"),
    LED("led"),
    TALK("talk"),
    WAIT("wait"),
    ;
    private final String code;

    public static class Deserializer extends JsonDeserializer<Command> {
        @Override
        public Command deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return fromCode(p.getValueAsString());
        }
    }

    Command(String code) {
        this.code = code;
    }

    public static Command fromCode(String code) {
        for (Command c: values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        return UNKNOWN;
    }
}
