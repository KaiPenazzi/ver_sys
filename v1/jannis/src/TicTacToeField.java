import java.util.Scanner;

public class TicTacToeField
{
    private static int width;
    private static int height;
    private static int k;
    private static String[][] field;

    public static void get_field()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Gebe die Breite des Spielfelds an.");
        width = scanner.nextInt();

        System.out.println("Gebe die Höhe des Spielfelds an.");
        height = scanner.nextInt();

        System.out.println("Gebe die Anzahl an Steine in einer Reihe an.");
        k = scanner.nextInt();

        field = new String[height][width];
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

    public static boolean set_cross(int row, int col, String username)
    {
        if (row < 0 || row >= height || col < 0 || col >= width)
        {
            return false;
        }

        if (!TicTacToeField.getField()[row][col].equals("empty"))
        {
            return false;
        }
        TicTacToeField.getField()[row][col] = username;
        Json_converter.create_JSON(Json_converter.Message_type.ACTION);
        return true;
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
