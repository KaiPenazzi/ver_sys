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
                obj = build_init(obj);
                break;
            case ACTION:
                obj = build_action(obj, row, col);
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
        System.out.println(obj.toString(4));
    }

    public static JSONObject build_init(JSONObject obj)
    {
        obj.put("type", "init");
        obj.put("field", TicTacToeField.getField());
        obj.put("k", TicTacToeField.getK());
        obj.put("score", Spiellogik.getPunktestand());

        return obj;
    }

    public static JSONObject build_action(JSONObject obj, int row, int col)
    {
        obj.put("type", "action");
        obj.put("usr", Spiellogik.getPlayer().getUsername());
        obj.put("y", row);
        obj.put("x", col);

        return obj;
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

                JSONObject pointObj = obj.getJSONObject("score");
                HashMap<String,Integer> ranking = new HashMap<String,Integer>();
                //Rangliste parsen
                for ( Object key : pointObj.keySet()){
                    int val = pointObj.getInt((String) key);
                    ranking.put((String) key, val);
                }
                Spiellogik.setPunktestand(ranking);
                TicTacToeGUI.instance.updateRanking(ranking);

                TicTacToeField.set_game(field, rows, cols, obj.getInt("k"));
                break;
            case "action":
                System.out.println("Action Message detected");
                TicTacToeField.set_cross(obj.getString("usr"), obj.getInt("y"), obj.getInt("x"), false);
                TicTacToeGUI.instance.set_gui_cross(obj.getString("usr"), obj.getInt("y"), obj.getInt("x"));


                String username = obj.getString("usr");

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
