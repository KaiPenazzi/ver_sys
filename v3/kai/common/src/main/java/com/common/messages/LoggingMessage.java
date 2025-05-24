package com.common.messages;

public class LoggingMessage implements Message {
    public String type;
    public Body body;

    public static class Body {
        public int timestamp;
        public String start_node;
        public String end_node;
        public String msg_type;
        public int sum;
    }
}
