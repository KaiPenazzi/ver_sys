package com.common.udp;

import com.common.messages.Message;

@FunctionalInterface
public interface Listener {
    void onMessage(Message msg);
}
