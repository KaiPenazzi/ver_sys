package com.common.messages;

public class ResultMessage implements Message {
    public String type;
    public Body body;

    public static class Body {
        public int result;
    }
}
