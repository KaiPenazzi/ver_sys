import org.json.JSONObject;

public class ResultMessage extends Message {
        private int result;
    public ResultMessage() {
        messageType = MessageType.result;
    }

    @Override
    public String JsonFromMessage() {
        JSONObject msg = new JSONObject();


        return super.JsonFromMessage();
    }
}
