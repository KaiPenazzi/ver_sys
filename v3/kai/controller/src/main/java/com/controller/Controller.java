package com.controller;

import java.net.InetSocketAddress;
import java.util.List;

import com.common.NetUtil;
import com.common.Config.Node;
import com.common.PrintUtil;
import com.common.messages.LoggingMessage;
import com.common.messages.Message;
import com.common.messages.ResultMessage;
import com.common.messages.StartMessage;
import com.common.udp.Client;

public class Controller {
    private InetSocketAddress self;
    private Client udp_client;
    private NodesManager nodes;

    private int count_e = 0;
    private int count_i = 0;

    public Controller(NodesManager node_manager, InetSocketAddress self)
            throws Exception {
        this.nodes = node_manager;
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
        this.nodes.stop();
    }

    private void recvMessage(Message msg) {
        switch (msg) {
            case ResultMessage result:
                System.out.println("Total Messages: " + (this.count_i + this.count_e));
                System.out.println("Info: " + this.count_i);
                System.out.println("Echo: " + this.count_e);
                System.out.println("got result sum: " + result.body.result);
                this.count_e = 0;
                this.count_i = 0;
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
        this.log(new LoggingMessage(this.self, address, msg.getClass().getSimpleName(), -1));
    }

    public List<Node> getNodes() {
        return this.nodes.getNodes();
    }

    private void log(LoggingMessage msg) {
        switch (msg.body.msg_type) {
            case "EchoMessage":
                this.count_e++;
                break;
            case "InfoMessage":
                this.count_i++;
                break;
        }
        PrintUtil.printLog(msg);
    }

}
