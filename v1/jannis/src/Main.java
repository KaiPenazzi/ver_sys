import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws SocketException {
        Spiellogik.start_new_Game();
        Json_converter.create_JSON(Json_converter.Message_type.INIT);
    }
}