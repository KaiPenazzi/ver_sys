package com.common.messages;

import java.net.InetSocketAddress;

import com.common.NetUtil;

public class InfoMessage implements Message {
    public Body body;

    public static class Body {
        public String from;

        public Body() {
        }
    }

    public InfoMessage() {
    }

    public InfoMessage(InetSocketAddress from) {
        this.body = new Body();
        this.body.from = NetUtil.ToString(from);
    }

    public boolean equalMsg(InfoMessage o) {
        boolean isEqual = true;

        if (!this.body.from.equals(o.body.from)) {
            isEqual = false;
        }

        return isEqual;
    }
}
