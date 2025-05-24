package com.common;

import org.junit.jupiter.api.Test;

import com.common.messages.InfoMessage;

public class Parser {
    @Test
    public void testParseInfoMessage() {
        var info_msg = new InfoMessage("idk where im from");

        String json2 = JsonUtil.toJson(info_msg);
        try {
            var msg = JsonUtil.parse(json2);
            assert info_msg.equalMsg((InfoMessage) msg) : "Parsed message does not match original";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
