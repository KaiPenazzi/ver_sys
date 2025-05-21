import java.util.List;

public class Node {
    private String ip;
    private int port;
    private String upward_Node;

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

    private int neighbours_informed;
    private List<Node> neighbours;
    private boolean informed;
    private int sum;
    private boolean initiator;

    public Node(String address, int sum, List<Node> neighbours) {
        neighbours_informed = 0;
        informed = false;
        this.sum = sum;
        String ip = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        initiator = false;
    }

}
