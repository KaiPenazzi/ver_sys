import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    private Board gameBoard;
    private HashMap<String,Integer>  ranking;
    public static List<Player> players;
    public static int myPort;
    private boolean isRunning = false;


    //Player liste variabel
    public Game(Board gameBoard, HashMap<String,Integer>  ranking, int myPort) {
        this.gameBoard = gameBoard;
        this.ranking = ranking;
        players = new ArrayList<Player>();
        //für Labor bereits vorher bekannt
        //players.add(new Player(1111,"192.168.5.2"));
        //players.add(new Player(2222,"192.168.5.14"));
        //players.add(new Player(3333,"192.168.5.6"));
        //für Labor Teil 2 nur  friend bekannt
        players.add(new Player(1111,"127.0.0.1"));

        this.myPort = myPort;
    }

    /**
     * bei klick auf ein Feld wird geprüft ob Feld frei und gegebenfalls der auslösende Spieler eingetragen.
     * anschließend wird geprüft ob k-Felder gleich markiert sind und die Punktzahl des Spielers ggf erhöht
     * @param x Koordinate im Board
     * @param y Koordinate im Board
     * @param username Spieler der eingetragen wird
     * @return true, wenn Zug legal False wenn nicht
     */
    public boolean move(int x, int y, String username){

        if(this.getGameBoard().getBoard()[y][x].equals("none")){
            this.getGameBoard().getBoard()[y][x]= username;

            int score = this.gameBoard.checkBoard();

            if (score > 0 )
            {
                incRanking(username, score);
            }

            return true;
        }
        return false;

    }


    public HashMap<String, Integer> getRanking() {
        return this.ranking;
    }

    private void incRanking(String username, int score){
        this.getRanking().put(username, this.getRanking().getOrDefault(username, 0) + score);
    }

    public Board getGameBoard() {
        return this.gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

     public void setRanking(HashMap<String, Integer> ranking){
        this.ranking = ranking;
     }
}
