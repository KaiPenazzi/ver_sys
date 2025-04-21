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

    @Override
    public String toString() {
        return "Player{" +
                "usr='" + usr + '\'' +
                ", port=" + port +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        else {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return port == player.port &&
                usr.equals(player.usr) &&
                ip.equals(player.ip);
    }
    }

}
