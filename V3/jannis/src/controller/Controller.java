package controller;

import Messages.LogMsg;
import Messages.Message;
import Node.Node;
import communication.UDPClient;
import communication.UDPServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Controller
{
    public final String ownIP;
    public final Path configFile;
    public final UDPServer server;
    public final UDPClient client;
    public final List<configNode> nodes;

    public Controller(String ownIP, String configFile)
    {
        this.ownIP = ownIP;
        this.configFile = Path.of(configFile);
        int port = Integer.parseInt(ownIP.split(":")[1]);
        server = new UDPServer(port, this);
        client = new UDPClient();
        nodes = new ArrayList<>();
    }

    // Deployer functionality
    public void getConfigNodes() throws IOException {
        String content = new String(Files.readString(configFile));

        // In JSONObject parsen
        JSONObject root = new JSONObject(content);
        JSONArray nodes = root.getJSONArray("nodes");

        for (int i = 0; i < nodes.length(); i++)
        {
            JSONObject node = nodes.getJSONObject(i);
            String ip = node.getString("address");
            int storageVal = node.getInt("storage");
            JSONArray neigh = node.getJSONArray("neighbors");
            List<String> neighbours = new ArrayList<>();

            for (int j = 0; j < neigh.length(); j++)
            {
                neighbours.add(neigh.getString(j));
            }

            configNode newNode = new configNode(ip, storageVal, neighbours);
            this.nodes.add(newNode);
        }
    }

    // Logger functionality
    public void recMsg(String msg)
    {
        JSONObject obj = new JSONObject(new JSONTokener(msg));
        JSONObject body = obj.getJSONObject("body");

        String type = body.getString("type");
        int timestamp = body.getInt("timestamp");
        String start_node = body.getString("start_node");
        String end_node = body.getString("end_node");
        int sum = body.getInt("sum");

        LogMsg logMsg = new LogMsg(start_node, end_node, getMsgType(type), sum);

        System.out.println(logMsg.toString());

    }

    public Message.MessageType getMsgType(String msg)
    {
        switch (msg.toLowerCase())
        {
            case "start":
                return Message.MessageType.start;
            case "i":
                return Message.MessageType.info;
            case "e":
                return Message.MessageType.echo;
            case "result":
                return Message.MessageType.result;
        }
        //unreachable
        return Message.MessageType.echo;
    }
}
