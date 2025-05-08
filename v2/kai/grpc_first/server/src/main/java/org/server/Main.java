package org.server;

import java.net.URI;

public class Main {
    public static void main(String[] args) {
        System.out.println("usage: java -jar server.jar <port>\nif you haven't specified port, default port is 3000");
        int port = (args.length > 0 && args[0] != null) ? Integer.parseInt(args[0]) : 3000;
        URI backup = (args.length > 1 && args[1] != null) ? URI.create(args[1]) : URI.create("http://localhost:3001");

        MyServer server = new MyServer(port, backup);
        server.start();
        server.stop();
    }
}
