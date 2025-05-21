import java.util.List;

public class PlayerMessageData extends MessageData{
    private  List<Player> players;

    public PlayerMessageData(List<Player> players) {
        this.players = players;
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
