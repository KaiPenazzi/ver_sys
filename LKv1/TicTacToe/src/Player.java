public class Player {

    private String nickname;
    private int port;
    private String ip;

    public Player(String nickname, int port, String ip){
        this.ip = ip;
        this.nickname = nickname;
        this.port = port;
    }
    public Player(int port, String ip){
        this.ip = ip;
        this.port = port;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
