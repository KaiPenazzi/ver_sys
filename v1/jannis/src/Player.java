import java.util.Scanner;

public class Player
{
    private static String username;
    private static int port;

    public Player(String username, int port)
    {
        this.username = username;
        this.port = port;
    }

    public static Player get_player()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Gib deinen Usernamen an: ");
        username = scanner.nextLine();

        System.out.println("Gib deinen Port an: ");
        port = scanner.nextInt();

        return new Player(username, port);
    }

    public static String getUsername() {
        return username;
    }

    public static int getPort() {
        return port;
    }

}
