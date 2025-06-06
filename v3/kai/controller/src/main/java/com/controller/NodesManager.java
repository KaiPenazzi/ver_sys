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
import com.common.PrintUtil;
import com.common.Config.Node;

class NodesManager {
    private Config config;
    private Path node;
    private String user;

    public NodesManager(Path config, Path node, String user) throws Exception {
        this.config = JsonUtil.parse_config(Files.readString(config));
        PrintUtil.printConfig(this.config);
        this.node = node;
        this.user = user;
    }

    public void start_nodes(InetSocketAddress controller) {
        new Thread(() -> {
            for (Node node : this.config.nodes) {
                List<String> command = new ArrayList<>();
                String addr = node.getAddr().getHostString();

                if (!addr.equals("localhost") && !addr.equals("127.0.0.1")) {
                    command.add("ssh");
                    command.add(this.user + "@" + addr);
                    command.add("-o StrictHostKeyChecking=no");
                }

                command.add("java");
                command.add("-jar");
                command.add(this.node.toString());
                command.add(String.valueOf(node.storage));
                command.add(node.address);
                command.add(NetUtil.ToString(controller));
                command.addAll(node.neighbors);

                try {
                    new ProcessBuilder(command)
                            .start();
                } catch (IOException e) {
                    System.out.println("could not start node: " + node.address);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public List<Node> getNodes() {
        return this.config.nodes;
    }

    public Node getNode(int index) {
        return this.config.nodes.get(index);
    }

    public void stop() {
        new Thread(() -> {
            for (Node node : this.config.nodes) {
                List<String> command = new ArrayList<>();
                String addr = node.getAddr().getHostString();

                if (!addr.equals("localhost") && !addr.equals("127.0.0.1")) {
                    command.add("ssh");
                    command.add(this.user + "@" + addr);
                    command.add("-o StrictHostKeyChecking=no");
                }

                command.add("pkill");
                command.add("-f");
                command.add("node.jar");

                try {
                    new ProcessBuilder(command)
                            .start();
                } catch (IOException e) {
                    System.out.println("could not kill jars");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
