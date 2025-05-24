package com.common;

import java.net.InetSocketAddress;

import org.junit.jupiter.api.Test;

class TestNetUtil {
    @Test
    public void testParseValidAddress() {
        String address = "localhost:8080";
        InetSocketAddress socketAddress = NetUtil.parse(address);
        assert socketAddress.getHostName().equals("localhost");
        assert socketAddress.getPort() == 8080;
    }

    @Test
    public void testToString() {
        String string_addr = "localhost:5001";
        InetSocketAddress inet_addr = new InetSocketAddress("localhost", 5001);
        String inet_str = NetUtil.ToString(inet_addr);
        assert inet_str.equals(string_addr);
    }
}
