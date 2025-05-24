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
}
