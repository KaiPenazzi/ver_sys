package com.common;

import java.util.List;

public class InitFile {
    public List<Node> nodes;

    public static class Node {
        public String address;
        public int storage;
        public List<String> neighbors;
    }
}
