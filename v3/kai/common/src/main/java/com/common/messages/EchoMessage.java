package com.common.messages;

public class EchoMessage implements Message {
    public String type;
    public Body body;

    public static class Body {
        public int sum;
    }
}
