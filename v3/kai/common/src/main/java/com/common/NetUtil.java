package com.common;

import java.net.InetSocketAddress;

public class NetUtil {
    public static InetSocketAddress parse(String address) {
        String[] parts = address.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid address: " + address);
        }
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        return new InetSocketAddress(host, port);
    }

    public static String ToString(InetSocketAddress address) {
        return address.getHostString() + ":" + address.getPort();
    }
}
