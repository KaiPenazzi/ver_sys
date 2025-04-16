import java.util.Scanner;

public class Player
{
    private String username;
    private String ip;
    private int port;

    public Player(String username, String ip, int port)
    {
        this.username = username;
        this.ip = ip;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
