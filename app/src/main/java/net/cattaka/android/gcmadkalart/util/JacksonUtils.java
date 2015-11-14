package net.cattaka.android.gcmadkalart.util;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacksonUtils {
    static ThreadLocal<ObjectMapper> mObjectMappers = new ThreadLocal<ObjectMapper>();

    public static <T> T getObject(JSONObject json, Class<T> object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(json.toString(), object);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getObject(String data, Class<T> object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(data, object);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> getObjects(String data, Class<T> object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            List<T> result = new ArrayList<T>();
            JSONArray staffings = new JSONArray(data);
            for (int i = 0; i < staffings.length(); i++) {
                result.add(mapper.readValue(staffings.getJSONObject(i).toString(), object));
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = mObjectMappers.get();
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            mObjectMappers.set(objectMapper);
        }
        return objectMapper;
    }
}
