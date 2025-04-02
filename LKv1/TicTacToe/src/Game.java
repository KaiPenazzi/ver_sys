import java.awt.*;
import java.util.HashMap;

public class Game {

    private Board gameBoard;
    private HashMap<String,Integer>  ranking;

    public Game(Board gameBoard, HashMap<String,Integer>  ranking) {
        this.gameBoard = gameBoard;
        this.ranking = ranking;
    }

    public void move( int x, int y, String username){


        if(this.getGameBoard().getBoard()[y][x].getNickname().equals("none")){
            this.getGameBoard().getBoard()[y][x].setNickname(username);
        }


    }


    public HashMap<String, Integer> getRanking() {
        return this.ranking;
    }

    private void incRanking(String username){
        this.getRanking().put(username, this.getRanking().getOrDefault(username, 0) +1);
    }

    public Board getGameBoard() {
        return this.gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }
}
