import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller {

    Deployer deployer;

    public static void main(String[] args) {
        try {
           // Datei einlesen
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

                Node node = new Node(address, storage, neighbors);
                nodes.add(node);
            }

            System.out.println("Anzahl Nodes: " + nodes.size());
            System.out.println("Anzahl der Kanten:" + countEdges(nodes));

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
