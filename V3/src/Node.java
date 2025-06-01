import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    private String ip;
    private int port;
    private String upward_Node;
    private String loggerIp;
    private int loggerPort;
    private String loggerAddress;

    private boolean informed;
    private int sum;
    private boolean initiator;
    private int neighbours_informed;
    //neighbours wird direkt als Liste von Adressen gespeichert
    private List<String> neighbours;

    public Node(int sum, String address, String loggerAddress, List<String> neighbours ) {
        neighbours_informed = 0;
        informed = false;
        this.sum = sum;
        this.ip = Operations.getIP(address);
        this.port = Operations.getPort(address);
        this.loggerAddress = loggerAddress;
        this.loggerIp = Operations.getIP(loggerAddress);
        this.loggerPort = Operations.getPort(loggerAddress);
        initiator = false;
        this.neighbours = neighbours;
        UDPCon.loggerAddress = loggerAddress;
    }

    public static void main(String[] args) throws InterruptedException, SocketException {

        //storage int> <self ip:port> <logger ip:port> [<neighbors ip:port>]
        if(args.length < 3){
            System.err.println("Wrong argument count");
            return;
        }
        int storage;
        try{
            storage = Integer.parseInt(args[0]);
        }catch (NumberFormatException e) {
            System.err.println("Invalid storage value: " + args[0]);
            return;
        }

        String selfAddress = args[1];
        String loggerAddress = args[2];
        List<String> neighbours = new ArrayList<>();
        System.out.println("Node " + selfAddress +" started" );
        neighbours = Arrays.asList(Arrays.copyOfRange(args, 3, args.length));

        Node node = new Node(storage, selfAddress, loggerAddress, neighbours);
        UDPCon udpCon= new UDPCon();
        udpCon.socketInitializer(node.port);
        //start receiving msgs
        while(node.getNeighbours_informed() != node.neighbours.size()){
            //System.out.println("warte auf msg");
            Message msg = UDPCon.recvMessage();
            if (msg == null) continue;
            LogMessage logMsg;
            switch (msg){
                case StartMessage startMessage:
                   // System.out.println("StartMsg erhalten: "+ node.getPort());
                    node.setInitiator(true);
                    node.setInformed(true);
                    InfoMessage iMsg = new InfoMessage();
                    iMsg.setFrom(selfAddress);
                    udpCon.sendToNeighbour(iMsg,selfAddress, node.getNeighbours());

                    break;
                case ResultMessage resultMessage:
                    //System.out.println("Resultmessage erhalten: "+ node.getPort());
                        break;
                case InfoMessage infoMessage:
                    //System.out.println("Info erhalten: "+ node.getPort());
                   // System.out.println("isInformed nach recv von info:" + node.isInformed());
                    node.incNeighboursInformed();
                    if(!node.isInformed()) {
                        node.setUpward_Node(infoMessage.getFrom());
                        node.setInformed(true);
                        //System.out.println("sende infoMessage an Nachbarn ausser upward");
                        infoMessage.setFrom(selfAddress);
                        udpCon.sendToNeighbour(infoMessage, selfAddress, node.getNeighbours(), node.getUpward_Node());
                    }
                    if (node.getNeighbours_informed() == node.getNeighbours().size()) {
                        if (node.isInitiator()) {
                            Message resultMessage = new ResultMessage(node.getSum());
                            udpCon.sendToAddress(resultMessage, loggerAddress, selfAddress);
                        } else {
                            Message echoMessage = new EMessage(node.getSum());
                            udpCon.sendToAddress(echoMessage, node.getUpward_Node(), selfAddress);
                        }
                    }
                    break;
                case EMessage eMessage:
                    //System.out.println("Echo erhalten: "+ node.getPort());
                    int sumBelow = eMessage.getSum();
                    node.setSum((sumBelow + node.getSum()));
                    node.incNeighboursInformed();
                    if(node.getNeighbours_informed() == node.getNeighbours().size()) {
                        if (node.isInitiator()) {
                            Message resultMessage = new ResultMessage(node.getSum());
                            udpCon.sendToAddress(resultMessage, loggerAddress, selfAddress);

                        } else {
                            Message echoMessage = new EMessage(node.getSum());
                            udpCon.sendToAddress(echoMessage, node.getUpward_Node(), selfAddress);
                        }
                    }
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + msg);
            }
        }
        udpCon.socketClose();
    }


    public List<String> getNeighbours() {
        return neighbours;
    }

    public int getNeighbours_informed() {
        return neighbours_informed;
    }

    public void setNeighbours_informed(int neighbours_informed) {
        this.neighbours_informed = neighbours_informed;
    }
    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUpward_Node() {
        return upward_Node;
    }

    public void setUpward_Node(String upward_Node) {
        this.upward_Node = upward_Node;
    }

    public boolean isInformed() {
        return informed;
    }

    public void setInformed(boolean informed) {
        this.informed = informed;
    }

    public void setNeighbours(List<String> neighbours) {
        this.neighbours = neighbours;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public boolean isInitiator() {
        return initiator;
    }

    public void setInitiator(boolean initiator) {
        this.initiator = initiator;
    }
    public void incNeighboursInformed(){
        this.neighbours_informed++;
    }

    public String getLoggerAddress() {
        return loggerAddress;
    }

    public void setLoggerAddress(String loggerAddress) {
        this.loggerAddress = loggerAddress;
    }
}
