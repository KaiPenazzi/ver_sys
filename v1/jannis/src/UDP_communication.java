import java.io.IOException;
import java.net.*;
public class UDP_communication {

    static String addresses [] = {"192.168.5.2", "192.168.5.14"}; // todo check IP Adresses in Lab

    public static void send_udp(String message) throws SocketException {
        //String address = "127.0.0.1";
        int[] ports = { 1111, 2222, 3333};

        try(DatagramSocket socket = new DatagramSocket())
        {
           for (int i = 0; i < ports.length; i++) {
               if (ports[i] != Spiellogik.getPlayer().getPort()) {
                   InetAddress serverIp = InetAddress.getByName(addresses[i]);
                   byte[] sendData = message.getBytes();

                   DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIp, ports[i]);
                   socket.send(sendPacket);
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void receive_udp() throws SocketException {
        int port = Spiellogik.getPlayer().getPort();
        byte[] receive_buf  = new byte[1024];

        try(DatagramSocket socket = new DatagramSocket(port))
        {
            System.out.println("Server läuft");

            while (true)
            {
                DatagramPacket receivePacket = new DatagramPacket(receive_buf, receive_buf.length);
                socket.receive(receivePacket);

                String receiveData = new String (receivePacket.getData(), 0, receivePacket.getLength());
                Json_converter.receive_JSON(receiveData);
                System.out.println("Empfangene Daten : " + receiveData);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
