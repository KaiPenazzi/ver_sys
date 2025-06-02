package Messages;

import org.json.JSONObject;

public class EchoMsg extends Message
{
    public EchoMsg()
    {
        super(MessageType.echo);
    }

    public String build_JSON(int sum)
    {
        JSONObject obj = new JSONObject();
        JSONObject body = new JSONObject();

        obj.put("type", "e");
        body.put("sum", sum);
        obj.put("body", body);

        return obj.toString();
    }
}
