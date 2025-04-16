import org.json.*;
import java.util.HashMap;

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
                obj.put("usr", Spiellogik.getPlayer().getUsername());
                obj.put("ip", Spiellogik.getPlayer().getIp());
                obj.put("port", Spiellogik.getPlayer().getPort());
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

                JSONObject pointObj = obj.getJSONObject("Punktestand");
                HashMap<String,Integer> ranking = new HashMap<String,Integer>();
                //Rangliste parsen
                for (Object key : pointObj.keySet()){
                    int val = pointObj.getInt((String) key);
                    ranking.put((String) key, val);
                }
                Spiellogik.setPunktestand(ranking);
                TicTacToeGUI.instance.updateRanking(ranking);

                TicTacToeField.set_game(field, rows, cols, obj.getInt("k"));
                break;
            case "action":
                System.out.println("Action Message detected");
                TicTacToeField.set_cross(obj.getString("username"), obj.getInt("row"), obj.getInt("col"), false);
                TicTacToeGUI.instance.set_gui_cross(obj.getString("username"), obj.getInt("row"), obj.getInt("col"));


                String username = obj.getString("username");

                if (!Spiellogik.getPunktestand().containsKey(username)) {
                    Spiellogik.getPunktestand().put(username, 0);
                    TicTacToeGUI.instance.updateRanking(Spiellogik.getPunktestand());
                }
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
