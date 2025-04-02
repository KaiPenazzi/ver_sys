import javax.swing.*;
import java.awt.*;
public class Board {
    public final int FIELDSIZE = 3;
    public final int k = 3;
    private Field[][] board;


    public Board() {
    this.board = new Field[FIELDSIZE][FIELDSIZE];
        for (int i = 0; i < FIELDSIZE; i++) {
            for (int j = 0; j < FIELDSIZE; j++) {
                this.board[i][j] = new Field();
            }
        }
    }

    public void setBoard(Field[][] b) {
        for (int i = 0; i < FIELDSIZE; i++) {
            for (int j = 0; j < FIELDSIZE; j++) {
                this.board[i][j] = b[i][j];
            }
        }
    }
        public Field[][] getBoard(){
        return this.board;
    }

    public boolean checkBoard(){
        boolean pointScored = false;
        if(this.checkRow()) {
            pointScored = true;

        }


        return false;
    }
    public boolean checkRow(){
        return false;
    }

    public boolean checkColumn(){
        return false;
    }

    public boolean checkDiagonal(){
        return false;
    }

    public void removeRow(){

    }

    public void printBoardTest(){
        for (int i = 0; i < FIELDSIZE; i++) {
            System.out.print("\n----------------\n");
            for (int j = 0; j < FIELDSIZE; j++) {
                System.out.print("|");
                if (this.getBoard()[i][j].getNickname().equals("none")) {
                    System.out.print("    ");
                }else {
                    System.out.print(this.getBoard()[i][j].getNickname());
                }
                if(j == FIELDSIZE -1)
                    System.out.print("|");
            }
        }
        System.out.print("\n----------------\n");
    }

}
