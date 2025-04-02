import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws SocketException {
        Spiellogik.start_new_Game();
        TicTacToeField.set_cross(0,0, "Sinnaj004");
        TicTacToeField.set_cross(0,0, "Test");
        Json_converter.create_JSON(Json_converter.Message_type.INIT);
    }
}