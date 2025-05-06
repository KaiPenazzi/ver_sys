package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class ClientMain {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("enter Server Port: ");
        int port = Integer.parseInt(scanner.nextLine());

        String ip = "127.0.0.1";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        //ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();
        System.out.println("channel added");
        System.out.println("enter userid:");


        myClient client = new myClient(scanner.nextLine(), LogServiceGrpc.newBlockingStub(channel), LogServiceGrpc.newStub(channel));
        System.out.println("List of Commands:\n "
                            + "AddLog\n"
                            + "GetLog\n"
                            + "ListenLog\n"
                            + "UnlistenLog");
        System.out.println("Enter command");
        String userInput = scanner.nextLine();
        boolean exit = false;
        while (!exit){

            switch (userInput){
                case "AddLog":
                    System.out.println("enter Log:");
                    String l = scanner.nextLine();
                    client.addLog(l);
                    break;
                case  "GetLog":
                    client.getLog();
                    break;
                case "ListenLog":
                    client.listen(client.getUserId());
                    break;
                case "UnlistenLog":
                    client.unlisten(client.getUserId());
                    break;
                case"xx":
                    System.out.println("execution stopped");
                    exit = true;
                    break;
                default:
                    System.out.println("Command not found");
                    break;
            }
            System.out.println("enter next command:");
            userInput = scanner.nextLine();
        }

        channel.shutdown();

    }
}
