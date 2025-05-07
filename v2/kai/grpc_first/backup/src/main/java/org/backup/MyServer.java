package org.backup;

import java.io.IOException;
import java.net.URI;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class MyServer {
    Server server;
    int port = 3000;

    public MyServer(int port) {
        this.port = port;
    }

    public void start() {
        server = ServerBuilder.forPort(port)
                .addService(new MyService())
                .build();

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            return;
        }

        try {
            System.out.println("Backup started on port: " + this.port);
            server.awaitTermination();
        } catch (InterruptedException e) {
            System.err.println("Server interrupted: " + e.getMessage());
        }
    }
}
