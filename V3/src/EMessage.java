import org.json.JSONObject;

public class EMessage extends Message {

    int sum;

    public EMessage() {
        messageType = MessageType.e;
    }

    @Override
    public String JsonFromMessage() {
        JSONObject msg = new JSONObject();

        return super.JsonFromMessage();
    }
}

