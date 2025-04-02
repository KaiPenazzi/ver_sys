import javax.xml.crypto.Data;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;


public class UDP_Com {
    public static void recv_UDP(){
        int port = 12345;
         try{
             DatagramSocket socket = new DatagramSocket(port);
             byte[] buf = new byte[1024];
             DatagramPacket packet = new DatagramPacket(buf, buf.length);
             System.out.println("waiting for Msg");
             socket.receive(packet);
             System.out.println("waiting for Msg");
             String recvMsg = new String(packet.getData(), 0 , packet.getLength());
             System.out.println(recvMsg);

             socket.close();

         } catch (Exception e) {
             e.printStackTrace();
         }


    }

    public static void send_UDP(){

        int port = 12345;
        String ip = "192.168.5.6";
        String msg = "Test Jannis";

        try{
            DatagramSocket socket = new DatagramSocket();

            byte[] data = msg.getBytes();
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
            socket.send(packet);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
