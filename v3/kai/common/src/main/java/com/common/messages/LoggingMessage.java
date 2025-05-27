package com.common.messages;

import java.net.InetSocketAddress;
import java.util.Date;

import com.common.NetUtil;

public class LoggingMessage implements Message {
    public Body body;

    public static class Body {
        public int timestamp;
        public String start_node;
        public String end_node;
        public String msg_type;
        public int sum;
    }

    public LoggingMessage() {
    }

    public LoggingMessage(int timestamp, String start_node, String end_node, String msg_type, int sum) {
        this.body = new Body();
        this.body.timestamp = timestamp;
        this.body.start_node = start_node;
        this.body.end_node = end_node;
        this.body.msg_type = msg_type;
        this.body.sum = sum;
    }

    public LoggingMessage(InetSocketAddress from, InetSocketAddress to, String msg_type, int sum) {
        this.body = new Body();
        this.body.timestamp = (int) (System.currentTimeMillis() / 1000);
        this.body.start_node = NetUtil.ToString(from);
        this.body.end_node = NetUtil.ToString(to);
        this.body.msg_type = msg_type;
        this.body.sum = sum;
    }

    public boolean equalMsg(LoggingMessage o) {
        boolean isEqual = true;

        if (this.body.timestamp != o.body.timestamp) {
            isEqual = false;
        }
        if (!this.body.start_node.equals(o.body.start_node)) {
            isEqual = false;
        }
        if (!this.body.end_node.equals(o.body.end_node)) {
            isEqual = false;
        }
        if (!this.body.msg_type.equals(o.body.msg_type)) {
            isEqual = false;
        }
        if (this.body.sum != o.body.sum) {
            isEqual = false;
        }

        return isEqual;
    }

    @Override
    public MessageType getMsgType() {
        return MessageType.LOG;
    }
}
