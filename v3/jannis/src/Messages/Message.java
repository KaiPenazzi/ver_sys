package Messages;

public abstract class Message
{
    MessageType type;

    public Message(MessageType type)
    {
        this.type = type;
    }

    public int getLatency()
    {
        return (int)(Math.random() * 99) + 1;
    }

    public enum MessageType
    {
        start,
        result,
        log,
        info,
        echo
    }
}
