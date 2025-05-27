package controller;

import Messages.LogMsg;
import Node.Node;
import communication.UDPClient;
import communication.UDPServer;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.nio.file.Path;

public class Controller
{
    public final String ownIP;
    public final Path configFile;
    public final UDPServer server;
    public final UDPClient client;

    public Controller(String ownIP, Path configFile)
    {
        this.ownIP = ownIP;
        this.configFile = configFile;
        int port = Integer.parseInt(ownIP.split(":")[1]);
        server = new UDPServer(port, this);
        client = new UDPClient();
    }

    public void recMsg(String msg)
    {
        JSONObject obj = new JSONObject(new JSONTokener(msg));
        JSONObject body = obj.getJSONObject("body");

        String type = body.getString("type");
        int timestamp = body.getInt("timestamp");
        String start_node = body.getString("start_node");
        String end_node = body.getString("end_node");
        int sum = body.getInt("sum");

        LogMsg logMsg = new LogMsg(start_node, end_node, type, sum);

    }
}
