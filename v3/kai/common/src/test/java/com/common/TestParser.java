package com.common;

import java.net.InetSocketAddress;

import org.junit.jupiter.api.Test;

import com.common.messages.EchoMessage;
import com.common.messages.InfoMessage;
import com.common.messages.LoggingMessage;
import com.common.messages.ResultMessage;
import com.common.messages.StartMessage;

public class TestParser {

    @Test
    public void testParseStart() {
        var start_msg = new StartMessage();

        String json = JsonUtil.toJson(start_msg);
        try {
            var msg = JsonUtil.parse(json);
            assert msg instanceof StartMessage : "Parsed message does not match original";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInfo() {
        var info_msg = new InfoMessage(new InetSocketAddress("123.1.2.3", 1234));

        String json = JsonUtil.toJson(info_msg);
        try {
            var msg = JsonUtil.parse(json);
            assert info_msg.equalMsg((InfoMessage) msg) : "Parsed message does not match original";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseEcho() {
        var echo_msg = new EchoMessage(5);

        String json = JsonUtil.toJson(echo_msg);
        try {
            var msg = JsonUtil.parse(json);
            assert echo_msg.body.sum == ((EchoMessage) msg).body.sum : "Parsed message does not match original";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseLogging() {
        var logging_msg = new LoggingMessage(
                1234567890,
                "start_node",
                "end_node",
                "msg_type",
                1);

        String json = JsonUtil.toJson(logging_msg);
        try {
            var msg = JsonUtil.parse(json);
            assert logging_msg.equalMsg((LoggingMessage) msg) : "Parsed message does not match original";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseResult() {
        var result_msg = new ResultMessage(1);

        String json = JsonUtil.toJson(result_msg);
        try {
            var msg = JsonUtil.parse(json);
            assert result_msg.body.result == ((ResultMessage) msg).body.result
                    : "Parsed message does not match original";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
