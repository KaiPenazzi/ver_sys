package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class BackupServer
{
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(2222)
                .addService(new BackupServerImpl())
                .build();

        System.out.println("Server läuft auf Port 2222...");
        server.start();
        server.awaitTermination();
    }
}
