package com.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.common.Config;
import com.common.JsonUtil;
import com.common.NetUtil;
import com.common.Config.Node;

class NodesManager {
    private Config config;

    private List<Process> processes = new ArrayList<>();

    public NodesManager(Path config) throws Exception {
        this.config = JsonUtil.parse_config(Files.readString(config));
    }

    public void start_nodes(InetSocketAddress controller) {
        for (Node node : this.config.nodes) {
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-jar");
            command.add("node.jar");
            command.add(String.valueOf(node.storage));
            command.add(node.address);
            command.add(NetUtil.ToString(controller));
            command.addAll(node.neighbors);

            try {
                processes.add(new ProcessBuilder(command)
                        .inheritIO()
                        .start());
            } catch (IOException e) {
                System.out.println("could not start node: " + node.address);
            }
        }
    }

    public List<Node> getNodes() {
        return this.config.nodes;
    }

    public Node getNode(int index) {
        return this.config.nodes.get(index);
    }

    public void stop() {
        this.processes.forEach(process -> {
            process.destroy();
        });
    }
}
