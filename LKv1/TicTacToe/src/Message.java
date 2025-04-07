public class Message{

    private String type;
    private MessageData data;

    /**
     * Message Datentyp der an Peers gesendet wird
     */
    public Message(){
        this.type = null;
        this.data = null;
    }

    public Message(String type, MessageData data){
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }
}
