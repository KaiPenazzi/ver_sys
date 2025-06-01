import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

public class LogMessage extends Message {

    private int timestamp;
    private String start_Node;
    private String end_Node;
    private String msg_type;
    int sum;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public LogMessage(String fromJSON) {
        messageType = "log";
        JSONObject obj = new JSONObject(fromJSON);
        this.messageType = obj.getString("type");

        JSONObject body = obj.getJSONObject("body");
        this.timestamp = body.getInt("timestamp");
        this.start_Node = body.getString("start_node");
        this.end_Node = body.getString("end_node");
        this.msg_type = body.getString("msg_type");
        this.sum = body.getInt("sum");


    }

    @Override
    public String toJSONString() {
        JSONObject body = new JSONObject();
        body.put("timestamp", timestamp);
        body.put("start_node", start_Node);
        body.put("end_node", end_Node);
        body.put("msg_type", msg_type);
        body.put("sum", sum);

        JSONObject obj = new JSONObject();
        obj.put("type", messageType);
        obj.put("body", body);

        return obj.toString(2); // hier mit Einrückungen
    }


    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getStart_Node() {
        return start_Node;
    }

    public void setStart_Node(String start_Node) {
        this.start_Node = start_Node;
    }

    public String getEnd_Node() {
        return end_Node;
    }

    public void setEnd_Node(String end_Node) {
        this.end_Node = end_Node;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
