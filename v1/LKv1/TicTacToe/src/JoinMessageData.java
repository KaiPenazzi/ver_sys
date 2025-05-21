public class JoinMessageData extends MessageData{

    private String usr;
    private int port;
    private String ip;


    public JoinMessageData(String usr, int port, String ip){
        this.usr = usr;
        this.ip = ip;
        this.port = port;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
