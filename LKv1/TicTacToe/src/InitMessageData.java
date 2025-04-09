import java.util.HashMap;

public class InitMessageData extends MessageData{
    public HashMap<String, Integer> points;
    private  Board board;

    public HashMap<String, Integer> getPoints() {
        return points;
    }


    public InitMessageData(HashMap<String,Integer> points, Board board){
        this.board = board;
        this.points = points;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPoints(HashMap<String, Integer> points) {
        this.points = points;
    }

    @Override
    public void printData(){
        System.out.println(this.points.toString());
    }
}
