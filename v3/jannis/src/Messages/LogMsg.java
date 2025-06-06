package Messages;

import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LogMsg extends Message
{
    public int timestamp;
    public String start_node;
    public String end_node;
    public MessageType msg_type;
    public int sum;

    public LogMsg()
    {
        super(MessageType.log);
    }
    public LogMsg(String fromInetAddress, String ToInetAddress, MessageType msgType, int sum)
    {
        super(MessageType.log);
        this.timestamp = (int) (System.currentTimeMillis() / 1000);
        this.start_node = fromInetAddress;
        this.end_node = ToInetAddress;
        this.msg_type = msgType;
        this.sum = sum;
    }

    public String build_JSON() {
        JSONObject obj = new JSONObject();
        JSONObject body = new JSONObject();

        body.put("timestamp", timestamp);
        body.put("start_node", start_node);
        body.put("end_node", end_node);

        switch (msg_type) {
            case start:
                body.put("msg_type", "s");
                break;
            case result:
                body.put("msg_type", "r");
                break;
            case log:
                body.put("msg_type", "l");
                break;
            case info:
                body.put("msg_type", "i");
                break;
            case echo:
                body.put("msg_type", "e");
                break;
        }

        body.put("sum", sum);

        obj.put("type", "log");
        obj.put("body", body);

        return obj.toString();
    }

    @Override
    public String toString() {
        String formattedTime = Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (msg_type == MessageType.info)
        {
            return formattedTime + "\t " + start_node + " -> " + end_node + "\t " + msg_type;

        }

        if (msg_type == MessageType.echo)
        {
            return formattedTime + "\t " + start_node + " -> " + end_node + "\t " + msg_type + " " + sum;
        }
        return null;
    }
}
