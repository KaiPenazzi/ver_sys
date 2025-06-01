package com.common;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TestPrinter {
    @Test
    public void testEdges() {
        Config config = new Config();

        Config.Node nodeA = new Config.Node("1.1.1.0:1", 100, Arrays.asList("1.1.1.1:1", "1.1.1.3:1", "1.1.1.0:2"));
        Config.Node nodeB = new Config.Node("1.1.1.1:1", 200, Arrays.asList("1.1.1.0:1", "1.1.1.2:1"));
        Config.Node nodeC = new Config.Node("1.1.1.2:1", 150, Arrays.asList("1.1.1.1:1"));
        Config.Node nodeD = new Config.Node("1.1.1.3:1", 200, Arrays.asList("1.1.1.0:1"));
        Config.Node nodeE = new Config.Node("1.1.1.0:2", 200, Arrays.asList("1.1.1.0:1"));

        config.nodes = Arrays.asList(nodeA, nodeB, nodeC, nodeD, nodeE);

        assert PrintUtil.edges(config).size() == 4;
    }
}
