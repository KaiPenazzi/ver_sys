package com.node;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.common.NetUtil;

class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println(
                    "Usage: java -jar node.jar <storage:int> <self:ip:port> <logger:ip:port> [<neighbors:ip:port>...]");
            System.exit(1);
        }

        int storage = Integer.parseInt(args[0]);
        InetSocketAddress self = NetUtil.parse(args[1]);
        InetSocketAddress logger = NetUtil.parse(args[2]);

        List<InetSocketAddress> neighbors = new ArrayList<>();
        for (int i = 3; i < args.length; i++) {
            neighbors.add(NetUtil.parse(args[i]));
        }

        try {
            Node node = new Node(storage, self, logger, neighbors);
            node.start();

        } catch (SocketException e) {
            System.out.println("could not create Node, maybe the ports are already in use");
            for (String arg : args) {
                System.out.print(arg + " ");
            }
            System.out.println();
            e.printStackTrace();
            System.exit(2);
        }
    }
}
