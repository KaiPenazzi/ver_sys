package Messages;

import org.json.JSONObject;

public class InfoMsg extends Message
{
    public InfoMsg()
    {
        super(MessageType.info);
    }

    public String build_JSON(String senderAddress)
    {
        JSONObject obj = new JSONObject();
        JSONObject body = new JSONObject();
        obj.put("type", "i");

        body.put("from", senderAddress);
        obj.put("body", body);

        return obj.toString();
    }

    public void recv_JSON(String data) throws InterruptedException
    {
        Thread.sleep(getLatency());
        System.out.println(data);
    }
}
