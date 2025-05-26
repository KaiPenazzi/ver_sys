import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public void sendMessage(String message, String serverIP) {
        String ip = serverIP.split(":")[0];
        int serverPort = Integer.parseInt(serverIP.split(":")[1]);

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(serverIP);
            byte[] messageBytes = message.getBytes();

            DatagramPacket packet = new DatagramPacket(
                    messageBytes, messageBytes.length,
                    address, serverPort
            );
            socket.send(packet);
            System.out.println("Gesendet: " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
