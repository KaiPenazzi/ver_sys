package com.common;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Config {
    public List<Node> nodes;
    Set pairs = new HashSet<String>();

    public static class Node {
        public String address;
        public int storage;
        public List<String> neighbors;

        public InetSocketAddress getAddr() {
            return NetUtil.parse(this.address);
        }

        public Node() {
        }

        public Node(String addr, int storage, List<String> neighbors) {
            this.address = addr;
            this.storage = storage;
            this.neighbors = neighbors;
        }
    }

}
