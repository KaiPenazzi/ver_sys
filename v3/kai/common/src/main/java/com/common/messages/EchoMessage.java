package com.common.messages;

public class EchoMessage implements Message {
    public Body body;

    public static class Body {
        public int sum;
        public String from;
    }

    public EchoMessage() {
    }

    public EchoMessage(int sum, String from) {
        this.body = new Body();
        this.body.sum = sum;
        this.body.from = from;
    }

}
