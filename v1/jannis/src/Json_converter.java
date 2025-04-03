import org.json.*;

public class Json_converter
{
    public enum Message_type
    {
        INIT,
        ACTION,
        JOIN
    }

    public static void create_JSON(Message_type type)
    {
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
                TicTacToeField.print_field();
                break;
            case JOIN:
                obj.put("type", "join");
                break;
            default:
                break;
        }
        System.out.println(obj.toString());
    }

    public static void receive_JSON(String message)
    {
        JSONObject obj = new JSONObject(new JSONTokener(message));


    }
}
