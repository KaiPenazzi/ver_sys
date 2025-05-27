package com.common;

import java.net.InetSocketAddress;
import java.util.List;

public class Config {
    public List<Node> nodes;

    public static class Node {
        public String address;
        public int storage;
        public List<String> neighbors;

        public InetSocketAddress getAddr() {
            return NetUtil.parse(this.address);
        }
    }
}
