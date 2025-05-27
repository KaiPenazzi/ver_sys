package com.common;

import java.util.Date;

import com.common.messages.LoggingMessage;

class PrintUtil {
    static public void printLogMsg(LoggingMessage msg) {
        Date date = new Date((long) msg.body.timestamp * 1000);

        System.out.println(date.toString());

    }
}
