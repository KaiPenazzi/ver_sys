package com.common.messages;

public class InfoMessage implements Message {
    public Body body;

    public static class Body {
        public String from;
    }

    public InfoMessage(String from) {
        this.body = new Body();
        this.body.from = from;
    }
}
