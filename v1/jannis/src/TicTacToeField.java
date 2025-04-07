import java.net.SocketException;
import java.util.Scanner;

public class TicTacToeField
{
    private static int width;
    private static int height;
    private static int k;
    private static String[][] field;

    public static void createField(int row, int col, int val)
    {
        width = col;
        height = row;
        k = val;
        field = new String[row][col];
        fill_field();
    }

    public static void fill_field()
    {
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                TicTacToeField.getField()[i][j] = "empty";
            }
        }
    }

    public static boolean set_cross(String username, int row, int col, boolean jsonMSG) throws SocketException {
        Player player = Spiellogik.getPlayer();

        if (row < 0 || row >= height || col < 0 || col >= width)
        {
            return false;
        }

        if (!TicTacToeField.getField()[row][col].equals("empty"))
        {
            return false;
        }
        TicTacToeField.getField()[row][col] = username;
        if(jsonMSG)
        {
            Json_converter.create_JSON(Json_converter.Message_type.ACTION, row, col);
        }
        return true;
    }

    public static void print_field()
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                System.out.print(field[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getK() {
        return k;
    }

    public static String[][] getField() {
        return field;
    }
}
