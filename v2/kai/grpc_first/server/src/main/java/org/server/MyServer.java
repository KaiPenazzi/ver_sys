package org.server;

import java.io.IOException;
import java.net.URI;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class MyServer {
    Server server;
    int port = 3000;
    MyService myService;
    BackupClient backupClient;

    public MyServer(int port, URI backup) {
        this.port = port;
        this.backupClient = new BackupClient(backup);
        this.myService = new MyService(backupClient);
    }

    public void start() {
        server = ServerBuilder.forPort(port)
                .addService(this.myService)
                .build();

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            return;
        }

        try {
            System.out.println("Server started on port: " + this.port);
            server.awaitTermination();
        } catch (InterruptedException e) {
            System.err.println("Server interrupted: " + e.getMessage());
        }
    }

    public void stop() {
        backupClient.stop();
    }
}
