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
    private List<String> neighbours;

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

    private boolean informed;
    private int sum;
    private boolean initiator;

    public Node(String address, int sum, List<String> neighbours) {
        neighbours_informed = 0;
        informed = false;
        this.sum = sum;
        this.ip = address.split(":")[0];
        this.port = Integer.parseInt(address.split(":")[1]);
        initiator = false;
        this.neighbours = neighbours;
    }

    public static void main(String[] args) {
            //
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
}
