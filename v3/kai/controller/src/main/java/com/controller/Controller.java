package com.controller;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.List;

import com.common.JsonUtil;
import com.common.NetUtil;
import com.common.Config.Node;
import com.common.messages.LoggingMessage;
import com.common.messages.Message;
import com.common.messages.ResultMessage;
import com.common.messages.StartMessage;
import com.common.udp.Client;

public class Controller {
    private InetSocketAddress self;
    private Client udp_client;
    private NodesManager nodes;

    public Controller(Path config, InetSocketAddress self)
            throws Exception {
        this.nodes = new NodesManager(config);
        this.self = self;
        this.udp_client = new Client(self, msg -> {
            this.recvMessage(msg);
        });
    }

    public void start() {
        this.udp_client.start();
        this.nodes.start_nodes(this.self);
    }

    public void stop() {
        this.udp_client.stop();
    }

    private void recvMessage(Message msg) {
        switch (msg.getClass().getSimpleName()) {
            case "ResultMessage":
                System.out.println("got result sum: " + ((ResultMessage) msg).body.result);
                break;

            default:
                this.log((LoggingMessage) msg);
                break;
        }
    }

    public void initiate(String node) throws NumberFormatException {
        StartMessage msg = new StartMessage();
        InetSocketAddress address = NetUtil.parse(this.nodes.getNode(Integer.parseInt(node)).address);
        this.udp_client.send(msg, address);
        System.out.println(JsonUtil.toJson(new LoggingMessage(this.self, address, msg.getClass().getSimpleName(), -1)));
    }

    public List<Node> getNodes() {
        return this.nodes.getNodes();
    }

    private void log(LoggingMessage msg) {
        System.out.println(JsonUtil.toJson(msg));
    }

}
