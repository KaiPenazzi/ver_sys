import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Spiellogik
{
    private static Map<String, Integer> punktestand = new HashMap<>();
    private static Player player;

    public static void start_new_Game(int row, int col, int val, boolean jsonmsg) throws SocketException {
        TicTacToeField.createField(row, col, val);
        if(jsonmsg) {
            Json_converter.create_JSON(Json_converter.Message_type.INIT, row, col); // row und col werden in dem Fall nicht verwendet
            System.out.println("Neues Spiel");
        }
        else {
            System.out.println("Spiel generiert");
        }
    }

    public static void check_for_point(int row, int col) {
        check_col(col);
        check_row(row);
    }

    public static void check_col(int col) {

        String[][] field = TicTacToeField.getField();
        String current_name = field[0][col];
        int counter = 0;

        for(int i = 0; i < TicTacToeField.getHeight(); i++)
        {
            if(current_name.equals(field[i][col]))
            {
                counter++;
            }
            else
            {
                current_name = field[i][col];
                counter = 0;
            }

            if (counter == TicTacToeField.getK())
            {
                System.out.println("Punkt gewonnen");
            }
        }
    }

    public static void check_row(int row) {

        String[][] field = TicTacToeField.getField();
        String current_name = field[row][0];
        int counter = 0;

        for(int i = 0; i < TicTacToeField.getHeight(); i++)
        {
            if(current_name.equals(field[row][i]))
            {
                counter++;
            }
            else
            {
                current_name = field[row][i];
                counter = 0;
            }

            if (counter == TicTacToeField.getK())
            {
                System.out.println("Punkt gewonnen für Spieler " + current_name);
            }
        }
    }
    public static Map<String, Integer> getPunktestand() {
        return punktestand;
    }

    public static void setPunktestand(Map<String, Integer> punktestand) {
        Spiellogik.punktestand = punktestand;
    }

    public static void setPlayer(Player player) {
        Spiellogik.player = player;
    }

    public static Player getPlayer() {
        return player;
    }
}
