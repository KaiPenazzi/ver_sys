import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Spiellogik
{
    private static Map<String, Integer> punktestand = new HashMap<>();
    private static Player player;

    public static void start_new_Game(int row, int col)
    {
        TicTacToeField.createField(row, col);
        Json_converter.create_JSON(Json_converter.Message_type.INIT);
        System.out.println("Neues Spiel");
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
