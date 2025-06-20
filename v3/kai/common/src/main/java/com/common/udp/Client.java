package com.common.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.common.JsonUtil;
import com.common.messages.Message;

public class Client {

    private DatagramSocket socket;
    private Listener listener;
    private Thread t;
    private boolean run = true;

    public Client(InetSocketAddress socket, Listener listener) throws SocketException {
        this.socket = new DatagramSocket(socket.getPort());
        this.listener = listener;
    }

    public void start() {
        t = new Thread(() -> {
            byte[] buf = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            while (run) {
                try {
                    socket.receive(packet);
                    String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                    try {
                        listener.onMessage(JsonUtil.parse(json));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("could not parse: " + json);
                    }
                } catch (SocketException e) {
                    if (run) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.println("could not receive packet");
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void stop() {
        this.run = false;
        this.socket.close();
        try {
            t.join();
        } catch (Exception e) {
            System.out.println("could not terminate thread clean");
        }
    }

    public void send(Message msg, InetSocketAddress to) {
        byte[] data = JsonUtil.toJson(msg).getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, to);

        try {
            this.socket.send(packet);
        } catch (Exception e) {
            System.out.println("could not send Message:" + data);
        }
    }
}
