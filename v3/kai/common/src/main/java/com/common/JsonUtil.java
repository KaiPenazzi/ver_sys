package com.common;

import com.common.messages.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Message parseUnknownMessage(String json) throws Exception {
        return mapper.readValue(json, Message.class);
    }

    public static <T> T parse(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }

    public static String toJson(Message obj) throws Exception {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
