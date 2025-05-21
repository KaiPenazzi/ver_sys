import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

public class LogMessage extends Message {

    private int timestamp;
    private String start_Node;
    private String end_Node;
    private String msg_type;
    int sum;

    public LogMessage() {
        messageType = MessageType.log;
    }

    @Override
    public String JsonFromMessage() {
        JSONObject msg = new JSONObject();

        return super.JsonFromMessage();
    }
}
