import java.util.Scanner;

public class Player
{
    private static String username;
    private static int port;

    public static void get_player()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Gib deinen Usernamen an: ");
        username = scanner.nextLine();

        System.out.println("Gib deinen Port an: ");
        port = scanner.nextInt();
    }

    public static String getUsername() {
        return username;
    }

    public static int getPort() {
        return port;
    }

}
