import org.json.JSONObject;

public class EMessage extends Message {

    int sum;

    public EMessage() {
        messageType = "e";
    }

    @Override
    public String toJSONString() {
        JSONObject body = new JSONObject();
        body.put("sum", this.getSum());

        JSONObject obj = new JSONObject();
        obj.put("type", this.messageType);
        obj.put("body", body);

        return obj.toString(2);
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}

