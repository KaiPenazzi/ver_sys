import org.json.JSONObject;

public class Operations {

    public static String getIP(String address) {
        return  address.split(":")[0];
    }
    public static int getPort(String address){
        return Integer.parseInt(address.split(":")[1]);
    }

    public static String extractType(String jsonString) {
            JSONObject obj = new JSONObject(jsonString);
            return obj.getString("type");

    }
}
