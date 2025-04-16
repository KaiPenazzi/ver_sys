import java.io.IOException;
import java.net.*;
public class UDP_communication {

    //String addresses [] = {"192.168.5.2", "192.168.5.8"}; // todo check IP Adresses in Lab

    public static void send_udp(String message) throws SocketException {

        for (Player p : Spiellogik.getPlayer_list()) {
            try(DatagramSocket socket = new DatagramSocket())
            {
                if (p != Spiellogik.getPlayer()) {
                    InetAddress serverIp = InetAddress.getByName(p.getIp());
                    byte[] sendData = message.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIp, p.getPort());
                    socket.send(sendPacket);

                    System.out.println("Gesendete Nachricht: " + message);;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void send_udp_to_specific_member(String message, String ip, int port) throws SocketException {

        try(DatagramSocket socket = new DatagramSocket())
        {
            InetAddress serverIp = InetAddress.getByName(ip);
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIp, port);
            socket.send(sendPacket);

            System.out.println("Gesendete Nachricht: " + message);;

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
