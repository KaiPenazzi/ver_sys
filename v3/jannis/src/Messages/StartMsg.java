package Messages;

import org.json.JSONObject;

public class StartMsg extends Message
{
    public StartMsg()
    {
        super(Message.MessageType.start);
    }

    public String build_JSON()
    {
        JSONObject obj = new JSONObject();
        obj.put("type", "start");

        return obj.toString();
    }

    public void recv_JSON(String data) throws InterruptedException
    {
        Thread.sleep(getLatency());
    }
}
