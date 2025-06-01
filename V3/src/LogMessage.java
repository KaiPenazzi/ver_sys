import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LogMessage extends Message {

    private int timestamp;
    private String start_Node;
    private String end_Node;
    private String msg_type;
    int sum;


    public LogMessage(String fromJSON) {
        messageType = "log";
        JSONObject obj = new JSONObject(fromJSON);
        this.messageType = obj.getString("type");

        JSONObject body = obj.getJSONObject("body");
        this.timestamp = (int) (System.currentTimeMillis() / 1000);

        this.start_Node = body.getString("start_node");
        this.end_Node = body.getString("end_node");
        this.msg_type = body.getString("msg_type");
        this.sum = body.getInt("sum");
    }
    public LogMessage(InfoMessage msg, String End_node){
        messageType = "log";
        start_Node= msg.getFrom();
        end_Node = End_node;
        msg_type = "i";

        this.timestamp = (int) (System.currentTimeMillis() / 1000);

    }
    public LogMessage(EMessage msg, String start_Node, String end_Node){
        this.messageType = "log";
        this.msg_type = "e";
        this.start_Node = start_Node;
        this.end_Node = end_Node;
        this.timestamp = (int) (System.currentTimeMillis() / 1000);

        this.sum = msg.getSum();
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

        return obj.toString(2);
    }

    @Override
    public String toString(){
         //Problem: in schnittstelle haben wir timestamp als int definiert -> Date funktioniert nur mit longs korrekt
        //Lösung : DateTimeFormatter für Instant objekt
        Instant instant = Instant.ofEpochSecond(this.timestamp);
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());

        String log = this.msg_type +" Message sent from: "+ this.start_Node + " to: "+ this.end_Node + ". Sum: "+ this.sum + " at " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(zdt) ;
        return log;
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

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

}
