import org.json.JSONObject;

public class StartMessage extends Message {

    public StartMessage() {
        messageType = "start";
    }
    public StartMessage(String json) {
        JSONObject obj = new JSONObject(json);
        this.messageType = obj.getString("type");
        // Kein body, daher nichts weiter
    }

    @Override
    public String toJSONString() {
        JSONObject obj = new JSONObject();
        obj.put("type", messageType);
        return obj.toString(2);
    }
}
