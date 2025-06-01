import java.awt.*;
import java.net.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UDPCon {

    public static String loggerAddress = "127.0.0.1:1111";
    public static DatagramSocket socket;

    public void socketInitializer(int port) throws SocketException {
        if (socket == null || socket.isClosed()){
            socket = new DatagramSocket(port);
        }
    }
    public  void socketClose(){
        if(socket!= null && !socket.isClosed()){
            socket.close();
        }

    }
    public  void sendToAddress(Message msg, String to, String from){
        try {
            int port = Operations.getPort(to);
            String ip = Operations.getIP(to);


            byte[] data = msg.toJSONString().getBytes();
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            this.socket.send(packet);
            //System.out.println(msg.getMessageType() + " message sent to Node: " + to);

            switch(msg){
                case InfoMessage infoMessage:
                    log(infoMessage,to);
                    break;
                case EMessage eMessage:
                    log(eMessage,to, from );
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendToNeighbour(Message msg, String from, List<String> neighbours) {
        String jsonString = msg.toJSONString();
        for (String NeighbourAddress : neighbours) {
            try {
                int port = Operations.getPort(NeighbourAddress);
                String ip = Operations.getIP(NeighbourAddress);


                byte[] data = jsonString.getBytes();
                InetAddress address = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
                //System.out.println(msg.getMessageType() + " message sent");
                switch(msg){
                    case InfoMessage infoMessage:
                        log(infoMessage,NeighbourAddress);
                        break;
                    case EMessage eMessage:
                        log(eMessage,from, NeighbourAddress);
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void sendToNeighbour(Message msg, String from, List<String> neighbours, String upwardNode) {
        String jsonString = msg.toJSONString();
        for (String NeighbourAddress : neighbours) {
            if(!(NeighbourAddress.equals(upwardNode))){
                try {
                    int port = Operations.getPort(NeighbourAddress);
                    String ip = Operations.getIP(NeighbourAddress);

                    byte[] data = jsonString.getBytes();
                    InetAddress address = InetAddress.getByName(ip);
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                    socket.send(packet);
                    //System.out.println(msg.getMessageType() + " message sent");

                    switch(msg){
                        case InfoMessage infoMessage:
                            log(infoMessage,NeighbourAddress);
                            break;
                        case EMessage eMessage:
                            log(eMessage,from, NeighbourAddress);
                            break;
                        default:
                            break;
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        }
    }


    /**
     * empfängt JSon String erzeugt daraus richtigen Messagetype und returned Message
     * Außerdem: Schicke Nachricht an Logger
     */
    public static Message recvMessage() throws InterruptedException {
        Message msg = null;
        //System.out.println("msg recv gestartet");

        try{
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            if (socket == null || socket.isClosed()) {
                System.err.println("Socket ist nicht initialisiert!");
                return null;
            }

            socket.receive(packet);
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 901));

            String recvMsg = new String(packet.getData(), 0 , packet.getLength());

            if(!recvMsg.equals("") &&  recvMsg != null){
                switch (Operations.extractType(recvMsg)){
                    case  "i":
                        //System.out.println("I message received");
                        msg = new InfoMessage(recvMsg);

                        break;
                    case "e":
                       // System.out.println("e message received");
                        msg = new EMessage(recvMsg);
                        break;
                    case "log":
                       // System.out.println("log message received");
                        msg = new LogMessage(recvMsg);
                        break;
                    case "result":
                        msg = new ResultMessage(recvMsg);
                        //System.out.println("result message received");
                        break;
                    case "start":
                        msg = new StartMessage(recvMsg);
                        //System.out.println("Start message received");
                        break;

                }
            }



        }catch (SocketTimeoutException e){
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public  void log(InfoMessage infoMessage, String end_Node){
        Message logMessage = new LogMessage(infoMessage, end_Node);
        sendToAddress(logMessage, loggerAddress, infoMessage.getFrom());

    }
    public  void log(EMessage eMessage, String start_Node, String end_Node){
        Message logMessage = new LogMessage(eMessage,end_Node,start_Node);
        sendToAddress(logMessage, loggerAddress, start_Node);
    }


}
