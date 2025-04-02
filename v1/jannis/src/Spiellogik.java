import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Spiellogik
{
    private static Map<String, Integer> punktestand = new HashMap<>();
    private static Player player;

    public static void start_new_Game()
    {
        TicTacToeField.get_field();
        Json_converter.create_JSON(Json_converter.Message_type.INIT);
    }

    public static Map<String, Integer> getPunktestand() {
        return punktestand;
    }

    public static void setPunktestand(Map<String, Integer> punktestand) {
        Spiellogik.punktestand = punktestand;
    }

    public static Player getPlayer() {
        return player;
    }
}
