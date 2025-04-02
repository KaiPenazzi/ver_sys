import java.io.IOException;
import java.net.*;
public class UDP_communication {

    String addresses [] = {"192.168.5.2", "192.168.5.8"}; // todo check IP Adresses in Lab

    public static void send_udp() throws SocketException {
        String address = "192.168.5.2";
        int port = 12345;
        String message = "Hallo Leon";

        try(DatagramSocket socket = new DatagramSocket())
        {
            InetAddress serverIp = InetAddress.getByName(address);
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIp, port);
            socket.send(sendPacket);

            System.out.println("Gesendete Nachricht: " + message);;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void receive_udp() throws SocketException {
        int port = 12345;
        byte[] receive_buf  = new byte[1024];

        try(DatagramSocket socket = new DatagramSocket(port))
        {
            System.out.println("Server läuft");

            while (true)
            {
                DatagramPacket receivePacket = new DatagramPacket(receive_buf, receive_buf.length);
                socket.receive(receivePacket);

                String receiveData = new String (receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Empfangene Daten : " + receiveData);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
