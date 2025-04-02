public class Message{

    private String type;
    private MessageData data;

    public Message(){
    
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
