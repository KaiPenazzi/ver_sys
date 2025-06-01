import org.json.JSONObject;

public class ResultMessage extends Message {
        private int result;
    public ResultMessage() {
        messageType = "result";
    }
    public ResultMessage(int result) {
        this.messageType = "result";
        this.result = result;
    }

    public ResultMessage(String json) {
        JSONObject obj = new JSONObject(json);
        this.messageType = obj.getString("type");
        JSONObject body = obj.getJSONObject("body");
        this.result = body.getInt("result");
    }

    @Override
    public String toJSONString() {
        JSONObject body = new JSONObject();
        body.put("result", result);

        JSONObject obj = new JSONObject();
        obj.put("type", messageType);
        obj.put("body", body);

        return obj.toString(2);
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
    @Override
    public String toString(){
        return "result is: " + this.result;
    }
}
