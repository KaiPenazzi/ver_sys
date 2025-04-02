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

        System.out.println(this.getGameBoard().getBoard()[x][y].getNickname());
        if(this.getGameBoard().getBoard()[x][y].getNickname().equals("none")){
            this.getGameBoard().getBoard()[x][y].setNickname(username);
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
