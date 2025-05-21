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
        //String ip = "192.168.5.14";
        //String ip = "192.168.5.6";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        //ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();
        System.out.println("channel added");
        System.out.println("enter userid:");


        myClient client = new myClient(scanner.nextLine(), LogServiceGrpc.newBlockingStub(channel), LogServiceGrpc.newStub(channel));
        System.out.println("List of Commands:\n "
                            + "AddLog\n"
                            + "GetLog\n"
                            + "ListenLog\n"
                            + "UnlistenLog\n"
                            + "CrashLog\n"
                            + "RestoreLog\n");
        System.out.println("Enter command");
        String userInput = scanner.nextLine();
        boolean exit = false;
        while (!exit){

            switch (userInput){
                case "AddLog":

                    client.addLog();
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
                case "CrashLog":
                    System.out.println("enter password: ");
                    String psw = scanner.nextLine();
                    client.crashLog(psw);
                    break;
                case "RestoreLog":
                    System.out.println("enter password: ");
                    String pw = scanner.nextLine();
                    client.restoreLog(pw);
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
