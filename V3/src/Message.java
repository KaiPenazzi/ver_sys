import org.json.JSONObject;

public abstract class Message {

    enum MessageType {
        i,
        e,
        start,
        result,
        log,
    }

    MessageType messageType;

    public String JsonFromMessage() {
        JSONObject msg = new JSONObject();

        return "";
    }


}
