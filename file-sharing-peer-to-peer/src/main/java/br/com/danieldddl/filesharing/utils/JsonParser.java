package br.com.danieldddl.filesharing.utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonParser {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static byte[] toJson (final Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException("Error while serializing object: ", e);
        }
    }

    public static <T> T fromJson (final byte[] json, final Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (final IOException e) {
            throw new IllegalStateException("Error while deserializing json: ", e);
        }
    }

}
