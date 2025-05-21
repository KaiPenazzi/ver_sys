import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Node
{
    private String ip;
    private int port;

    private List<Node> neighbors = new ArrayList<>();
    private String upward_node_ip;

    private int neigh_informed;
    private int storage_val;
    private int sum;

    private boolean initiator;
    private boolean informed;

    public Node (String address, int storage_val, List<Node> neighbors)
    {
        ip = address.split(":")[0];
        port = Integer.parseInt(address.split(":")[1]);

        this.neighbors = neighbors;
        upward_node_ip = "";

        neigh_informed = 0;
        this.storage_val = storage_val;
        sum = storage_val;

        initiator = false;
        informed = false;
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

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
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
}
