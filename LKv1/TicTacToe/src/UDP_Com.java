import java.net.*;


public class UDP_Com {
    private static DatagramSocket socket;

    /**
     * Anfangs wird der Socket einmal für den Port geöffnet um so mehrfache Öffnungsversuche zu verhindern
     * @param port
     */
    public static void socketInitializer(int port) throws SocketException {
        if (socket == null || socket.isClosed()){
            socket = new DatagramSocket(port);
        }
    }
    public static void socketClose(){
        if(socket!= null && !socket.isClosed()){
            socket.close();
        }

    }

    /**
     * empfange UDP Nachricht in Json Format. Parse auf Message Objekt
     * @return Message Objekt aus übertragenem Json File
     */
    public static Message recv_UDP(){
        Message message = new Message();
         try{
             socket.setSoTimeout((100));
             byte[] buf = new byte[1024];
             DatagramPacket packet = new DatagramPacket(buf, buf.length);

             socket.receive(packet);
             if (packet.getPort() == socket.getLocalPort()){
                 return null;
             }

             String recvMsg = new String(packet.getData(), 0 , packet.getLength());
             if(!recvMsg.equals("") &&  recvMsg != null){
                 message = Msg_Conversion.processIncomingMessage(recvMsg);
             }
             System.out.println(message.getType() + " message recveived");

             message = Msg_Conversion.processIncomingMessage(recvMsg);

         }catch (SocketTimeoutException e){
             return null;
         }
         catch (Exception e) {
             e.printStackTrace();
         }
         return message;

    }

    /**
     *  sende eine Nachricht per UDP an alle Peers (nicht an sich selbst)
     *  die message wird erst in ein Json Objekt serialisiert und dann gesendet
     * @param message zuvor erstellte Message
     *
     */
    public static void send_UDP(Message message){
        for (Player p : Game.players  ){
            try{
            int port = p.getPort();
            String ip = p.getIp();
            if(port != Game.myPort) {
                String jsonMessage = Msg_Conversion.createJSONFromMessage(message);
                DatagramSocket socket = new DatagramSocket();
                byte[] data = jsonMessage.getBytes();
                InetAddress address = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
                System.out.println(message.getType() + " message sent");
                socket.close();
            }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
