package Messages;

public class ResultMsg extends Message
{
    public ResultMsg()
    {
        super(MessageType.result);
    }

    public void build_JSON()
    {

    }

    public void recv_JSON(String data)
    {
        //emtpy
    }
}
