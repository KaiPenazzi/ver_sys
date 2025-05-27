package com.controller;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.common.NetUtil;

class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar controller.jar <self:ip:port> <PathToIni.json:String>");
            System.exit(1);
        }

        InetSocketAddress self = NetUtil.parse(args[0]);
        Path ini = Paths.get(args[1]);

        Controller controller = null;

        try {
            controller = new Controller(ini, self);
            controller.start();

            Scanner scanner = new Scanner(System.in);
            String input = "";

            System.out.println("commands: init, exit");
            do {
                input = scanner.nextLine();

                switch (input) {
                    case "init":
                        controller.getNodes().forEach(Node -> {
                            System.out.println(Node.address);
                        });
                        System.out.println("which node to initiate:");
                        String node = scanner.nextLine();
                        try {
                            controller.initiate(node);
                        } catch (NumberFormatException e) {
                            System.out.println("number could not be parsed to a number");
                        }
                        break;

                }
            } while (!input.equals("exit"));

            controller.stop();
        } catch (Exception e) {
            System.out.println("could not create controller");
            e.printStackTrace();
        } finally {
            if (controller != null) {
                controller.stop();
            }
        }
    }
}
