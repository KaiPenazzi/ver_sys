package com.common.messages;

public class StartMessage implements Message {
    @Override
    public MessageType getMsgType() {
        return MessageType.START;
    }
}
