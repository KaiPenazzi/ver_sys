package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class ClientMain
{
    static LogClient client;
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ManagedChannel channel = getChannel("localhost", 50051);
        LogServiceGrpc.LogServiceStub stub = LogServiceGrpc.newStub(channel);
        LogServiceGrpc.LogServiceBlockingStub blockingStubb = LogServiceGrpc.newBlockingStub(channel);
        client = new LogClient("Sinnaj004", blockingStubb,  stub);

        System.out.print("$ ");
        while (true)
        {
            String command = scanner.nextLine();
            parse(command);
        }
    }

    public static ManagedChannel getChannel(String host, int port)
    {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }

    public static void parse(String command)
    {
        switch (command)
        {
            case "add":
                System.out.println("Enter a Log Text: ");
                Scanner scanner = new Scanner(System.in);
                String log = scanner.nextLine();
                client.addLog(log);
                System.out.println("$ ");
                break;
            case "get":
                client.getLog();
                break;
            case "listen":
                client.listen();
                System.out.println("$" );
                break;
            case "unlisten":
                client.unlisten();
                break;
            default:
                System.out.println("Command " + command + " not available");
        }
    }
}
