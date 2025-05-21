package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class LogServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new LogServiceImpl())
                .build();

        System.out.println("Server läuft auf Port 50051...");
        server.start();
        server.awaitTermination();
    }
}
