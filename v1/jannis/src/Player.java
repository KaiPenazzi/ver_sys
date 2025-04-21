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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                      // gleiche Referenz
        if (obj == null || getClass() != obj.getClass()) return false; // ungleicher Typ

        Player other = (Player) obj;                       // Typumwandlung
        return port == other.port &&                       // Werte vergleichen
                username.equals(other.username) && ip.equals(other.ip);
    }
}
