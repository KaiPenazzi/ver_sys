package communication;

import Node.Node;
import controller.Controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    private final int port;
    private final Node node;
    private final Controller controller;

    public UDPServer(int port, Node node) {
        this.port = port;
        this.node = node;
        this.controller = null;
    }

    public  UDPServer(int port, Controller controller) {
        this.port = port;
        this.controller = controller;
        this.node = null;
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                if (node == null && controller != null) {
                    controller.recMsg(received);
                }
                else
                {
                    node.recMsg(received);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
