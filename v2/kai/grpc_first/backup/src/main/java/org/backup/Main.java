package org.backup;

class Main {
    public static void main(String[] args) {
        System.out.println("usage: java -jar server.jar <port>\nif you haven't specified port, default port is 3001");
        int port = (args.length > 0 && args[0] != null) ? Integer.parseInt(args[0]) : 3001;

        MyServer server = new MyServer(port);
        server.start();
    }
}
