import org.json.JSONObject;

public class InfoMessage extends Message {

    String from;

    public InfoMessage() {
        this.messageType = "i";

    }

    public InfoMessage(String fromJSON) {
        JSONObject obj = new JSONObject(fromJSON);
        this.messageType = obj.getString("type");
        this.from = obj.getJSONObject("body").getString("from");

    }

    @Override
    public String toJSONString(){
        JSONObject body = new JSONObject();
        body.put("from", from);

        JSONObject obj = new JSONObject();
        obj.put("type", messageType);
        obj.put("body", body);

        return obj.toString(2);
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
