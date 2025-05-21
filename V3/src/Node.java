import java.util.List;

public class Node {
    private String ip;
    private int port;
    private Node upward_Node;

    private int neighbours_informed;
    private List<Node> neighbours;
    private boolean informed = false;
    private int sum;


    private Node(){

    }
}
