
public abstract class Message {

    String messageType;

    abstract public  String toJSONString() ;

    public String getMessageType(){

        return this.messageType;

    }



}

enum eMsgTypes{
    E("e"),
    I("i"),
    START("start"),
    RESULT("result"),
    LOG("log");

    private final String value;

    eMsgTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
