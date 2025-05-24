package com.common.messages;

public class InfoMessage implements Message {
    public Body body;

    public static class Body {
        public String from;

        public Body() {
        }
    }

    public InfoMessage() {
    }

    public InfoMessage(String from) {
        this.body = new Body();
        this.body.from = from;
    }

    public boolean equalMsg(InfoMessage o) {
        boolean isEqual = true;

        if (!this.body.from.equals(o.body.from)) {
            isEqual = false;
        }

        return isEqual;
    }
}
