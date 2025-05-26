package Messages;

import org.json.JSONObject;

public class LogMsg extends Message
{
    public LogMsg()
    {
        super(MessageType.log);
    }

    public String build_JSON(String startNode, String endNode, MessageType msgType, int teilsumme)
    {
        JSONObject obj = new JSONObject();
        JSONObject body = new JSONObject();

        //Timestamp
        body.put("start_node", startNode);
        body.put("end_node", endNode);

        if (msgType == MessageType.echo)
        {
            body.put("msg_type", "e");
        }
        else
        {
            body.put("msg_type", "i");
        }

        body.put("sum", teilsumme);

        obj.put("type", "log");
        obj.put("body", body);

        return obj.toString();
    }

    public void recv_JSON(String data) throws InterruptedException
    {
        Thread.sleep(getLatency());
        System.out.println(data);
    }
}
