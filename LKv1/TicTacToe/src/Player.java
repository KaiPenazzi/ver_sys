public class Player {

    private String usr;
    private int port;
    private String ip;

    public Player(String usr, int port, String ip){
        this.ip = ip;
        this.usr = usr;
        this.port = port;
    }
    public Player(int port, String ip){
        this.ip = ip;
        this.port = port;
    }
    public void setUsr(String nickname) {
        this.usr = nickname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getUsr() {
        return usr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
