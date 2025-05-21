import org.json.JSONObject;

public class InfoMessage extends Message {

    String from;

    public InfoMessage() {
        messageType = MessageType.i;
    }

    public InfoMessage(String fromJSON) {
        messageType = MessageType.i;

    }

    @Override
    public String JsonFromMessage(){
        JSONObject msg = new JSONObject();

        return "";
    }


}
