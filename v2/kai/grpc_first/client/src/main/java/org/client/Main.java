package org.client;

import java.net.URI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                "usage: java -jar client.jar <user> <url>\nif you haven't specified user and url, default user is 'default' and default url is 'localhost:3000'");

        String usr = (args.length > 0 && args[0] != null) ? args[0] : "default";
        String url = (args.length > 1 && args[1] != null) ? args[1] : "localhost:3000";

        MyClient client = new MyClient(usr, URI.create("dummy://" + url));

        client.start();

        System.out.println("Commands: add, get, listen, unlisten, exit");

        Scanner scanner = new Scanner(System.in);
        String input = "";
        do {
            input = scanner.nextLine();

            switch (input) {
                case "add":
                    System.out.println("Enter log message:");
                    String message = scanner.nextLine();
                    client.addLog(message);
                    break;

                case "get":
                    client.getLog();
                    break;

                case "listen":
                    client.listenLog();
                    break;

                case "unlisten":
                    client.unlistenLog();
                    break;

                default:
                    System.out.println("Commands: add, get, listen, unlisten, exit");
                    break;
            }
        } while (!input.equals("exit"));

        client.stop();
    }
}
