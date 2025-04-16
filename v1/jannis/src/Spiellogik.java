import java.net.SocketException;
import java.util.*;

public class Spiellogik
{
    private static Map<String, Integer> punktestand = new HashMap<>();
    private static Player player;
    private static List<Player> player_list = new ArrayList<>();

    public static void start_new_Game(int row, int col, int val, boolean jsonmsg) throws SocketException {
        TicTacToeField.createField(row, col, val);
        TicTacToeGUI.instance.updateRanking(Spiellogik.getPunktestand());
        if(jsonmsg) {
            Json_converter.create_JSON(Json_converter.Message_type.INIT, row, col); // row und col werden in dem Fall nicht verwendet
            System.out.println("Neues Spiel");
        }
        else {
            System.out.println("Spiel generiert");
        }
    }

    public static void check_for_point(int row, int col) {
        check_row(row);
        check_col(col);
        check_diagonal();
        TicTacToeGUI.instance.updateRanking(punktestand);
    }

    public static void check_col(int col) {
        String[][] field = TicTacToeField.getField();
        String current_name = "";
        int counter = 0;

        for (int i = 0; i < TicTacToeField.getHeight(); i++) {
            String cell = field[i][col];
            if (!cell.equals("empty") && cell.equals(current_name)) {
                counter++;
            } else {
                current_name = cell;
                counter = cell.equals("empty") ? 0 : 1;
            }

            if (counter == TicTacToeField.getK()) {
                System.out.println("Punkt gewonnen für Spieler " + current_name);
                for (int offset = 0; offset < TicTacToeField.getK(); offset++) {
                    TicTacToeField.reset(i - offset, col);
                }
                TicTacToeGUI.instance.setzeFeldMitDaten(TicTacToeField.getField());
                punktestand.put(current_name, punktestand.getOrDefault(current_name, 0) + 1);
                return; // nach Treffer direkt abbrechen
            }
        }
    }


    public static void check_row(int row) {
        String[][] field = TicTacToeField.getField();
        String current_name = "";
        int counter = 0;

        for (int i = 0; i < TicTacToeField.getWidth(); i++) {
            String cell = field[row][i];
            if (!cell.equals("empty") && cell.equals(current_name)) {
                counter++;
            } else {
                current_name = cell;
                counter = cell.equals("empty") ? 0 : 1;
            }

            if (counter == TicTacToeField.getK()) {
                System.out.println("Punkt gewonnen für Spieler " + current_name);
                for (int offset = 0; offset < TicTacToeField.getK(); offset++) {
                    TicTacToeField.reset(row, i - offset);
                }
                TicTacToeGUI.instance.setzeFeldMitDaten(TicTacToeField.getField());
                punktestand.put(current_name, punktestand.getOrDefault(current_name, 0) + 1);
                return;
            }
        }
    }


    public static void check_diagonal() {
        String[][] field = TicTacToeField.getField();
        int height = TicTacToeField.getHeight();
        int width = TicTacToeField.getWidth();
        int k = TicTacToeField.getK();

        // ↘ Hauptdiagonalen
        for (int row = 0; row <= height - k; row++) {
            for (int col = 0; col <= width - k; col++) {
                String current_name = field[row][col];
                if (current_name.equals("empty")) continue;

                boolean win = true;
                for (int offset = 1; offset < k; offset++) {
                    if (!field[row + offset][col + offset].equals(current_name)) {
                        win = false;
                        break;
                    }
                }

                if (win) {
                    System.out.println("Punkt gewonnen für Spieler (↘) " + current_name);
                    // Felder zurücksetzen
                    for (int offset = 0; offset < k; offset++) {
                        TicTacToeField.reset(row + offset, col + offset);
                    }
                    TicTacToeGUI.instance.setzeFeldMitDaten(TicTacToeField.getField());
                    punktestand.put(current_name, punktestand.getOrDefault(current_name, 0) + 1);
                }
            }
        }

        // ↙ Nebendiagonalen
        for (int row = 0; row <= height - k; row++) {
            for (int col = k - 1; col < width; col++) {
                String current_name = field[row][col];
                if (current_name.equals("empty")) continue;

                boolean win = true;
                for (int offset = 1; offset < k; offset++) {
                    if (!field[row + offset][col - offset].equals(current_name)) {
                        win = false;
                        break;
                    }
                }

                if (win) {
                    System.out.println("Punkt gewonnen für Spieler (↙) " + current_name);
                    // Felder zurücksetzen
                    for (int offset = 0; offset < k; offset++) {
                        TicTacToeField.reset(row + offset, col - offset);
                    }
                    TicTacToeGUI.instance.setzeFeldMitDaten(TicTacToeField.getField());
                    punktestand.put(current_name, punktestand.getOrDefault(current_name, 0) + 1);
                }
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

    public static List<Player> getPlayer_list() {
        return player_list;
    }

    public static void setPlayer_list(List<Player> player_list) {
        Spiellogik.player_list = player_list;
    }

    public static void addPlayerToList(Player player)
    {
        player_list.add(player);
    }
}
