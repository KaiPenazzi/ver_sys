package Node;

import Messages.LogMsg;
import Messages.StartMsg;

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

        //System.out.println("Node: " + node.getInetAddress() +  " storeval: " + node.getStorage_val());
    }
}

