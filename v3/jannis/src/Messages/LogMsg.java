package Messages;

public class LogMsg extends Message
{
    public LogMsg()
    {
        super(MessageType.log);
    }

    public void build_JSON()
    {

    }

    public void recv_JSON(String data)
    {
        //emtpy
    }
}
