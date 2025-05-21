package Messages;

public class EchoMsg extends Message
{
    public EchoMsg()
    {
        super(MessageType.echo);
    }

    public void build_JSON()
    {

    }

    public void recv_JSON(String data)
    {
        //emtpy
    }
}
