package com.common;

import com.common.messages.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Message parse(String json) throws Exception {
        return mapper.readValue(json, Message.class);
    }

    public static String toJson(Message obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
