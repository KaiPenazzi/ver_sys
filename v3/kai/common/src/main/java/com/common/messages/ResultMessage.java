package com.common.messages;

public class ResultMessage implements Message {
    public Body body;

    public static class Body {
        public int result;
    }

    public ResultMessage() {
    }

    public ResultMessage(int result) {
        this.body = new Body();
        this.body.result = result;
    }
}
