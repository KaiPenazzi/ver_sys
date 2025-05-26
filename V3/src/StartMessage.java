import org.json.JSONObject;

public class StartMessage extends Message {

    public StartMessage() {
        messageType = MessageType.start;
    }

    @Override
    public String JsonFromMessage() {
        JSONObject msg = new JSONObject();

        return super.JsonFromMessage();
    }
}
