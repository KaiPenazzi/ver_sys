package com.common;

import com.common.messages.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Message parse(String msg) throws Exception {
        return mapper.readValue(msg, Message.class);
    }

    public static String toJson(Message msg) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Config parse_config(String config) throws Exception {
        return mapper.readValue(config, Config.class);
    }
}
