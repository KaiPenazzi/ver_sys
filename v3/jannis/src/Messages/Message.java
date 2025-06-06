package Messages;

public abstract class Message
{
    MessageType type;

    public Message(MessageType type)
    {
        this.type = type;
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
