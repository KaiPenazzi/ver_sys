package org.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class Main {
    public static void main(String[] args) {
        System.out.println("start Server");

        Server server = ServerBuilder.forPort(3000)
                .addService(new MyServer())
                .build();

        System.out.println("Server is starting...");

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            return;
        }

        try {
            System.out.println("Server started on port 1234");
            server.awaitTermination();
        } catch (InterruptedException e) {
            System.err.println("Server interrupted: " + e.getMessage());
        }
    }
}
