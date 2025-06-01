package controller;

import Messages.LogMsg;
import Messages.Message;
import Messages.StartMsg;
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
import java.util.Scanner;

public class Controller
{
    public final String ownIP;
    public final Path configFile;
    public UDPServer server;
    public UDPClient client;
    public final List<configNode> nodes;
    private List<Process> processes = new ArrayList<>();

    int iCounter = 0;
    int eCounter = 0;
    boolean running = true;
    int result;
    int nodesCount;
    int edgesCount;

    public Controller(String ownIP, String configFile)
    {
        this.ownIP = ownIP;
        this.configFile = Path.of(configFile);
        int port = Integer.parseInt(ownIP.split(":")[1]);
        nodes = new ArrayList<>();
        initCommunication();
    }

    public void initCommunication()
    {
        int port = Integer.parseInt(ownIP.split(":")[1]);
        new Thread(() -> {
            this.server = new UDPServer(port, this);
            server.start();
        }).start();

        client = new UDPClient();
    }

    public void start() throws IOException {
        this.getConfigNodes();
        for (configNode node : nodes)
        {
            List<String> cmd = this.buildCmd(node);

            try {
                processes.add(new ProcessBuilder(cmd)
                        .inheritIO()
                        .start());
            } catch (IOException e) {
                System.out.println("Failed to start Process with address " + node.getIp());
                e.printStackTrace();
            }
        }
        sendStart();

    }

    public void sendStart()
    {
        System.out.println("Das Netztwerk besteht aus " + nodesCount + " Knoten und " + edgesCount + " Kanten");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Gebe den Index des Initiators ein: ");
        int initiator = scanner.nextInt();

        if (initiator >= nodes.size())
        {
            System.out.println("Der Initiator kann nicht gefunden werden");
        }
        else
        {
            StartMsg startMsg = new StartMsg();
            this.client. sendMessage(startMsg.build_JSON(), nodes.get(initiator).getIp());
        }

    }

    public void stop()
    {
        for (Process process : processes)
        {
            process.destroy();
        }
    }

    public void printFinal()
    {
        System.out.println("Anzahl echos: " + eCounter + " Anzahl info: " + iCounter);
    }

    // java -jar node.jar <storage int> <self ip:port> <logger ip:port> [<neighbors ip:port>]
    public List<String> buildCmd(configNode node)
    {
        List<String> cmd = new ArrayList<>();
        cmd.add("java");
        cmd.add("-jar");
        cmd.add("out/artifacts/Node_jar/Node.jar");
        cmd.add(node.getStorageVal() + "");
        cmd.add(node.getIp());
        cmd.add(this.ownIP);
        cmd.addAll(node.getNeigh());

        return cmd;
    }
    // Deployer functionality
    public void getConfigNodes() throws IOException {
        String content = new String(Files.readString(configFile));

        // In JSONObject parsen
        JSONObject root = new JSONObject(content);
        JSONArray nodes = root.getJSONArray("nodes");

        nodesCount = nodes.length();

        int countEdges = 0;
        for (int i = 0; i < nodes.length(); i++)
        {
            JSONObject node = nodes.getJSONObject(i);
            String ip = node.getString("address");
            int storageVal = node.getInt("storage");
            JSONArray neigh = node.getJSONArray("neighbors");
            List<String> neighbours = new ArrayList<>();

            countEdges += neigh.length();
            for (int j = 0; j < neigh.length(); j++)
            {
                neighbours.add(neigh.getString(j));
            }

            configNode newNode = new configNode(ip, storageVal, neighbours);
            this.nodes.add(newNode);
        }
        edgesCount = countEdges / 2;
    }

    // Logger functionality
    public void recMsg(String msg)
    {
        JSONObject obj = new JSONObject(new JSONTokener(msg));
        JSONObject body = obj.getJSONObject("body");

        String type = obj.getString("type");
        if (type.equals("result"))
        {
            result = body.getInt("result");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            running = false;
        }

        if (type.equals("log"))
        {
            int timestamp = body.getInt("timestamp");
            String start_node = body.getString("start_node");
            String end_node = body.getString("end_node");
            int sum = body.getInt("sum");
            String msgType = body.getString("msg_type");

            LogMsg logMsg = new LogMsg(start_node, end_node, getMsgType(msgType), sum);

            if (msgType.equals("e"))
            {
                eCounter++;
            }

            if (msgType.equals("i"))
            {
                iCounter++;
            }
            System.out.println(logMsg.toString());

            if (!running)
            {
                printFinal();
                System.out.println("Result: " + result);
                stop();
            }
        }
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
