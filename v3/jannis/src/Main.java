import Messages.LogMsg;
import Messages.Message;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args)
    {
        if(args.length < 4)
        {
            System.out.println("To few Arguments given");
            System.exit(-1);
        }

        int storeValue = Integer.parseInt(args[0]);
        String ownIP = args[1];
        String loggerIP = args[2];
        List<String> neighIPs = Arrays.asList(args).subList(3, args.length);

        Node node = new Node(ownIP, storeValue, neighIPs, loggerIP);
        LogMsg msg = new LogMsg();

        node.getClient().sendMessage(msg.build_JSON("Start", "End", Message.MessageType.info, 4), "127.0.0.1:1111");
    }
}

