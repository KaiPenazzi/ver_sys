public class Board {
    private int fieldSize;
    private int k;
    private String[][] board;



    public Board() {
        this.board = new String[3][3];;
    }

    /**
     * erstelle ein Spielbrett
     * Standartwert ist "none"
     * @param fieldSize
     * @param k
     */
    public Board(int fieldSize, int k ){
        this.fieldSize = fieldSize;
        this.k = k;
        this.board = new String[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                this.board[i][j] = "none";

            }
        }


    }

    public void setBoard(String[][] b) {
        this.board = b;
    }
    public String[][] getBoard(){
        return this.board;
    }


    /**
     * prüft ob auf Feld k gleiche sind. der einfachheit halber erst reihe dann spalte dann diagonale
     * @return 1 wenn kombination vorhanden 0 wenn nicht
     */
    public int checkBoard() {
        for (int x = 0; x < fieldSize; x++) {
            for (int n = 0; n <= fieldSize - k; n++) {
                String player = board[x][n];
                if (!player.equals("none") && checkRow(x, n, player)) {
                    for (int y = n; y < n + k; y++) {
                        board[x][y] = "none";
                    }
                    return 1;
                }
            }
        }
        for (int y = 0; y < fieldSize; y++) {
            for (int n = 0; n <= fieldSize - k; n++) {
                String player = board[n][y];
                if (!player.equals("none") && checkColumn(n, y, player)) {
                    for (int x = n; x < n + k; x++) {
                        board[x][y] = "none";
                    }
                    return 1;
                }
            }
        }
        for (int x = 0; x <= fieldSize - k; x++) {
            for (int col = 0; col <= fieldSize - k; col++) {
                String player = board[x][col];
                if (!player.equals("none") && checkDiagonal1(x, col, player)) {
                    for (int i = 0; i < k; i++) {
                        board[x + i][col + i] = "none";
                    }
                    return 1;
                }
            }
        }
        for (int x = 0; x <= fieldSize - k; x++) {
            for (int y = k - 1; y < fieldSize; y++) {
                String player = board[x][y];
                if (!player.equals("none") && checkDiagonal2(x, y, player)) {
                    for (int i = 0; i < k; i++) {
                        board[x + i][y - i] = "none";
                    }
                    return 1;
                }
            }
        }

        return 0;
    }

    //Hilfsfunktionen für Reihen/Spalten/DiagonalenCheck
    private boolean checkRow(int x, int y, String player) {
        for (int i = y; i < y + k; i++) {
            if (!board[x][i].equals(player)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int x, int y, String player) {
        for (int i = x; i < x + k; i++) {
            if (!board[i][y].equals(player)) {
                return false;
            }
        }
        return true;
    }
    private boolean checkDiagonal1(int x, int y, String player) {
        for (int i = 0; i < k; i++) {
            if (!board[x + i][y + i].equals(player)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDiagonal2(int startRow, int startCol, String player) {
        for (int i = 0; i < k; i++) {
            if (!board[startRow + i][startCol - i].equals(player)) {
                return false;
            }
        }
        return true;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }
    public int getFieldSize(){
        return this.fieldSize;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}
