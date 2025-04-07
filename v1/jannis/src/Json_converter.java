import org.json.*;

import java.net.SocketException;

public class Json_converter
{
    public enum Message_type
    {
        INIT,
        ACTION,
        JOIN
    }

    public static void create_JSON(Message_type type, int row, int col) throws SocketException {
        JSONObject obj = new JSONObject();

        switch (type)
        {
            case INIT:
                obj.put("type", "init");
                obj.put("field", TicTacToeField.getField());
                obj.put("k", TicTacToeField.getK());
                obj.put("Punktestand", Spiellogik.getPunktestand());
                break;
            case ACTION:
                obj.put("type", "action");
                obj.put("username", Spiellogik.getPlayer().getUsername());
                obj.put("row", row);
                obj.put("col", col);
                //TicTacToeField.print_field();
                break;
            case JOIN:
                obj.put("type", "join");
                break;
            default:
                break;
        }
        UDP_communication.send_udp(obj.toString());
        System.out.println(obj.toString());
    }

    public static void receive_JSON(String message) throws SocketException {
        JSONObject obj = new JSONObject(new JSONTokener(message));

        switch (obj.getString("type"))
        {
            case "init":
                System.out.println("Init Message detected");
                JSONArray field = obj.getJSONArray("field");

                int rows = field.length(); // Anzahl der Zeilen
                int cols = 0;
                if (rows > 0) {
                    cols = field.getJSONArray(0).length(); // Anzahl der Spalten in der ersten Zeile
                }

                TicTacToeField.set_game(field, rows, cols, obj.getInt("k"));
                break;
            case "action":
                System.out.println("Action Message detected");
                TicTacToeField.set_cross(obj.getString("username"), obj.getInt("row"), obj.getInt("col"), false);
                TicTacToeGUI.instance.set_gui_cross(obj.getString("username"), obj.getInt("row"), obj.getInt("col"));
                //TicTacToeField.print_field();
                break;
            case "join":
                System.out.println("Join Message detected");
                create_JSON(Message_type.INIT, 0, 0); // row und col werden nicht verwendet
                break;
            default:
                break;
        }
    }
}
