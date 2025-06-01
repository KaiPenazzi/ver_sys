package Node;

import Messages.*;
import communication.UDPClient;
import communication.UDPServer;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    private String ip;
    private int port;

    private List<String> neighbors = new ArrayList<>();
    private String upward_node_ip;
    private String logger_ip;

    private int neigh_informed;
    private int storage_val;
    private int sum;

    private boolean initiator;
    private boolean informed;

    private UDPServer server;
    private UDPClient client;

    public Node (String address, int storage_val, List<String> neighbors, String loggerIp)
    {
        ip = address.split(":")[0];
        port = Integer.parseInt(address.split(":")[1]);

        this.neighbors = neighbors;
        upward_node_ip = "";
        this.logger_ip = loggerIp;

        neigh_informed = 0;
        this.storage_val = storage_val;
        sum = storage_val;

        initiator = false;
        informed = false;

        initCommunication();
    }

    public void initCommunication()
    {
        new Thread(() -> {
            server = new UDPServer(this.getPort(), this);
            server.start();
        }).start();

        client = new UDPClient();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<String> neighbors) {
        this.neighbors = neighbors;
    }

    public String getUpward_node_ip() {
        return upward_node_ip;
    }

    public void setUpward_node_ip(String upward_node_ip) {
        this.upward_node_ip = upward_node_ip;
    }

    public int getNeigh_informed() {
        return neigh_informed;
    }

    public void setNeigh_informed(int neigh_informed) {
        this.neigh_informed = neigh_informed;
    }

    public int getStorage_val() {
        return storage_val;
    }

    public void setStorage_val(int storage_val) {
        this.storage_val = storage_val;
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

    public boolean isInformed() {
        return informed;
    }

    public void setInformed(boolean informed) {
        this.informed = informed;
    }

    public String getLogger_ip() {
        return logger_ip;
    }

    public void setLogger_ip(String logger_ip) {
        this.logger_ip = logger_ip;
    }

    public UDPServer getServer() {
        return server;
    }

    public void setServer(UDPServer server) {
        this.server = server;
    }

    public UDPClient getClient() {
        return client;
    }

    public void setClient(UDPClient client) {
        this.client = client;
    }

    public String getInetAddress()
    {
        return this.getIp() + ":" + this.getPort();
    }

    public void sendLog(Message.MessageType type, String to) throws InterruptedException {
        LogMsg msg = new LogMsg(this.getInetAddress(), to, type, sum);
        client.sendMessage(msg.build_JSON(), logger_ip);
    }

    public int getLatency()
    {
        return (int)(Math.random() * 99) + 1;
    }

    public void recMsg(String msg) throws InterruptedException {
        //System.out.println("Empfangen: " + msg);
        JSONObject obj = new JSONObject(new JSONTokener(msg));

        
        switch(obj.getString("type"))
        {
            case "result":
                break;
            case "e":
                JSONObject body = obj.getJSONObject("body");
                neigh_informed++;

                int sum = body.getInt("sum");
                this.sum += sum;

                break;
            case "i":
                JSONObject body2 = obj.getJSONObject("body");
                neigh_informed++;

                if (!informed)
                {
                    informed = true;
                    upward_node_ip = body2.getString("from");

                    InfoMsg infoMsg = new InfoMsg();
                    for (int i = 0; i < neighbors.size(); i++)
                    {
                        if (!neighbors.get(i).equals(upward_node_ip))
                        {
                            client.sendMessage(infoMsg.build_JSON(this.getInetAddress()), neighbors.get(i));
                            sendLog(Message.MessageType.info, neighbors.get(i));
                        }

                    }
                }
                break;
            case "start":

                this.informed = true;
                this.initiator = true;


                InfoMsg infoMsg = new InfoMsg();
                for (int i = 0; i < neighbors.size(); i++)
                {
                    client.sendMessage(infoMsg.build_JSON(getInetAddress()), neighbors.get(i));
                    sendLog(Message.MessageType.start, neighbors.get(i));
                }
                break;
        }

        if (neigh_informed == neighbors.size())
        {
            if(initiator)
            {
                ResultMsg resultMsg = new ResultMsg();
                client.sendMessage(resultMsg.build_JSON(sum), logger_ip);
            }
            else
            {
                EchoMsg echoMsg = new EchoMsg();
                client.sendMessage(echoMsg.build_JSON(sum), upward_node_ip);
                sendLog(Message.MessageType.echo, upward_node_ip);
            }
        }
    }
}
