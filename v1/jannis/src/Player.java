import java.util.Scanner;

public class Player
{
    private String username;
    private int port;

    public Player(String username, int port)
    {
        this.username = username;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

}
