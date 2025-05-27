package com.common.messages;

public class EchoMessage implements Message {
    public Body body;

    public static class Body {
        public int sum;
    }

    public EchoMessage() {
    }

    public EchoMessage(int sum) {
        this.body = new Body();
        this.body.sum = sum;
    }
}
