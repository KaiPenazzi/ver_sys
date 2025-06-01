import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Controller {

    public static void main(String[] args) throws SocketException {

        String jarPath = "C:\\Users\\leonk\\IdeaProjects\\ver_sys\\V3\\out\\artifacts\\node_jar\\node.jar";

        Scanner scanner;
        scanner = new Scanner(System.in);
        System.out.println("enter Logger Address");
        //UDPCon.loggerAddress = scanner.nextLine();
        Logger logger = new Logger(UDPCon.loggerAddress);
        UDPCon udpCon = new UDPCon();
        udpCon.socketInitializer(Operations.getPort(UDPCon.loggerAddress));
        try {

            String network = new String(Files.readAllBytes(Paths.get("mynet.json")));

            JSONObject root = new JSONObject(network);
            JSONArray nodesArray = root.getJSONArray("nodes");

            List<Node> nodes = new ArrayList<>();

            for (int i = 0; i < nodesArray.length(); i++) {
                JSONObject nodeObj = nodesArray.getJSONObject(i);
                String address = nodeObj.getString("address");
                int storage = nodeObj.getInt("storage");

                JSONArray neighborsArray = nodeObj.getJSONArray("neighbors");
                List<String> neighbors = new ArrayList<>();

                for (int j = 0; j < neighborsArray.length(); j++) {
                    neighbors.add(neighborsArray.getString(j));
                }

                Node node = new Node(storage,address,UDPCon.loggerAddress, neighbors);
                nodes.add(node);
            }
            Deployer deployer = new Deployer(nodes,UDPCon.loggerAddress,jarPath);
            deployer.deploy();
            System.out.println("Anzahl Nodes: " + nodes.size());
            System.out.println("Anzahl der Kanten:" + countEdges(nodes));

            System.out.println("Initiatorknoten angeben: ");
            scanner = new Scanner(System.in);
            String initiatorNode = scanner.nextLine();
            Message startMessage = new StartMessage();
            udpCon.sendToAddress(startMessage,initiatorNode, logger.getAddress());
            while(true){
               Message msg =  logger.recv();
               System.out.println(msg.toString());
               if (msg.getMessageType().equals("result")){
                   System.out.println("Anzahl i msgs: "+ logger.getInfoCounter());
                   System.out.println("Anzahl e msgs: "+ logger.getEchoCounter());
                   break;
                }

            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int countEdges(List<Node> nodes) {
        int edges = 0;
        for (Node node : nodes) {
            edges += node.getNeighbours().size();
        }
        return edges;
    }
}
