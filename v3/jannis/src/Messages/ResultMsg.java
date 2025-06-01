package Messages;

import org.json.JSONObject;

public class ResultMsg extends Message
{
    public ResultMsg()
    {
        super(MessageType.result);
    }

    public String build_JSON(int result)
    {
        JSONObject obj = new JSONObject();
        JSONObject body = new JSONObject();

        body.put("result", result);

        obj.put("type", "result");
        obj.put("body", body);

        return obj.toString();
    }
}
