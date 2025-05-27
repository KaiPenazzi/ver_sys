package com.node;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

import com.common.NetUtil;
import com.common.messages.EchoMessage;
import com.common.messages.InfoMessage;
import com.common.messages.LoggingMessage;
import com.common.messages.Message;
import com.common.messages.MessageType;
import com.common.messages.ResultMessage;
import com.common.udp.Client;

public class Node {
    private InetSocketAddress self;
    private Client upd_client;
    private InetSocketAddress logger;
    private List<InetSocketAddress> neighbors;

    private int storage;
    private int informed_neighbors = 0;
    private boolean informed = false;
    private int sum;
    private boolean initiator = false;
    private InetSocketAddress N;

    public Node(int storage, InetSocketAddress address, InetSocketAddress logger, List<InetSocketAddress> neighbors)
            throws SocketException {

        this.storage = storage;
        this.self = address;
        this.sum = storage;
        this.logger = logger;
        this.neighbors = neighbors;

        this.upd_client = new Client(address, msg -> {
            this.recvMessage(msg);
        });
    }

    public void start() {
        this.upd_client.start();
    }

    public void stop() {
        this.upd_client.stop();
    }

    private void reset() {
        this.informed_neighbors = 0;
        this.informed = false;
        this.sum = this.storage;
        this.initiator = false;
        this.N = null;
    }

    public void recvMessage(Message msg) {

        switch (msg.getMsgType()) {
            case MessageType.INFO:
                this.informed_neighbors++;
                InfoMessage info = (InfoMessage) msg;
                if (!informed) {
                    this.informed = true;
                    this.N = NetUtil.parse(info.body.from);

                    neighbors.forEach(
                            neighbour -> {
                                if (!neighbour.equals(this.N)) {
                                    InfoMessage new_info = new InfoMessage(this.self);
                                    this.send(new_info, neighbour);
                                }
                            });
                }
                break;

            case MessageType.ECHO:
                this.informed_neighbors++;
                var sum = ((EchoMessage) msg).body.sum;
                this.sum += sum;
                break;

            case MessageType.START:
                this.initiator = true;
                this.informed = true;

                neighbors.forEach(
                        neighbour -> {
                            InfoMessage new_info = new InfoMessage(this.self);
                            this.send(new_info, neighbour);
                        });
                break;
        }

        if (this.informed_neighbors == this.neighbors.size()) {
            if (this.initiator) {
                ResultMessage result = new ResultMessage(this.sum);
                this.send(result, this.logger);
            } else {
                EchoMessage result = new EchoMessage(this.sum);
                this.send(result, this.N);
            }
            this.reset();
        }
    }

    private void send(Message msg, InetSocketAddress to) {
        this.log(msg.getClass().getSimpleName(), to);
        this.upd_client.send(msg, to);
    }

    private void log(String type, InetSocketAddress to) {
        LoggingMessage log = new LoggingMessage(this.self, to, type, this.sum);
        upd_client.send(log, this.logger);
    }
}
