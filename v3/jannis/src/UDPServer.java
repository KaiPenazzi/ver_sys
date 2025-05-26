import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    private final int port;
    private final Node node;

    public UDPServer(int port, Node node) {
        this.port = port;
        this.node = node;
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server läuft auf Port " + port);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                node.recMsg(received);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
