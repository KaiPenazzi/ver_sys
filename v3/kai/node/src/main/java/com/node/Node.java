package com.node;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

import com.common.messages.Message;
import com.common.udp.Client;

public class Node {
    int storage;
    Client upd_client;
    InetSocketAddress logger;
    List<InetSocketAddress> neighbours;

    public Node(int storage, InetSocketAddress address, InetSocketAddress logger, List<InetSocketAddress> neighbours)
            throws SocketException {

        this.storage = storage;
        this.logger = logger;
        this.neighbours = neighbours;

        upd_client = new Client(address, msg -> {
            this.recvMessage(msg);
        });
    }

    public void start() {
        this.upd_client.start();
    }

    public void stop() {
        this.upd_client.stop();
    }

    public void recvMessage(Message msg) {

    }
}
