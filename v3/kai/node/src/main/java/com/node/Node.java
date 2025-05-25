package com.node;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

import com.common.NetUtil;
import com.common.messages.EchoMessage;
import com.common.messages.InfoMessage;
import com.common.messages.Message;
import com.common.messages.ResultMessage;
import com.common.udp.Client;

public class Node {
    private int storage;
    private InetSocketAddress self;
    private Client upd_client;
    private InetSocketAddress logger;
    private List<InetSocketAddress> neighbours;

    private int informed_neighbours = 0;
    private boolean informed = false;
    private int sum = 0;
    private boolean initiator = false;
    private InetSocketAddress N;

    public Node(int storage, InetSocketAddress address, InetSocketAddress logger, List<InetSocketAddress> neighbours)
            throws SocketException {

        this.storage = storage;
        this.self = address;
        this.sum = storage;
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
        this.informed_neighbours++;

        switch (msg.getClass().getSimpleName()) {
            case "InfoMessage":
                if (!informed) {
                    this.informed = true;
                    this.N = NetUtil.parse(((InfoMessage) msg).body.from);

                    neighbours.forEach(
                            neighbour -> {
                                if (!neighbour.equals(this.N)) {
                                    InfoMessage info = new InfoMessage(this.self);
                                    upd_client.send(info, neighbour);
                                }
                            });
                }
                break;

            case "EchoMessage":
                var sum = ((EchoMessage) msg).body.sum;
                this.sum += sum;
                break;
        }

        if (this.informed_neighbours == this.neighbours.size()) {
            if (this.initiator) {
                ResultMessage result = new ResultMessage(this.sum);
                upd_client.send(result, this.logger);
            } else {
                EchoMessage result = new EchoMessage(this.sum);
                upd_client.send(result, this.N);
            }
        }
    }
}
