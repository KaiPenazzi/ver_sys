import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Spiellogik
{
    private static Map<String, Integer> punktestand = new HashMap<>();
    private static Player player;

    public static void start_new_Game(int row, int col, int val, boolean jsonmsg) throws SocketException {
        TicTacToeField.createField(row, col, val);
        if(jsonmsg) {
            Json_converter.create_JSON(Json_converter.Message_type.INIT, row, col); // row und col werden in dem Fall nicht verwendet
            System.out.println("Neues Spiel");
        }
        else {
            System.out.println("Spiel generiert");
        }
    }

    public static Map<String, Integer> getPunktestand() {
        return punktestand;
    }

    public static void setPunktestand(Map<String, Integer> punktestand) {
        Spiellogik.punktestand = punktestand;
    }

    public static void setPlayer(Player player) {
        Spiellogik.player = player;
    }

    public static Player getPlayer() {
        return player;
    }
}
