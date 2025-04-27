package org.client;

import java.net.URI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String usr = (args.length > 0 && args[0] != null) ? args[0] : "default";
        String url = (args.length > 1 && args[1] != null) ? args[1] : "localhost:3000";

        MyClient client = new MyClient(usr, URI.create("dummy://" + url));

        client.start();

        client.addLog("Hello World");
        client.getLog();

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
                    System.out.println("Listening to logs...");
                    client.listenLog();
                    break;

                case "stop":
                    System.out.println("Stopping listener...");
                    client.unlistenLog();
                    break;

                default:
                    break;
            }
        } while (!input.equals("exit"));

        client.stop();
    }
}
