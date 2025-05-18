package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;


public class ServerMain{
    public static void main(String[] args) throws Exception{
        int port = 2222;
        int backupPort = 3333;
        Server backupServer = ServerBuilder.forPort(backupPort).addService(new myBackupServer()).build();
        Server server = ServerBuilder.forPort(port).addService(new myServer()).build();
        //Backup Server in Thread starten
        new Thread(() -> {
            try {
                backupServer.start();
                backupServer.awaitTermination();
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }).start();

        //Hauptserver in hauptthread
        server.start();
        System.out.println("Server is running on port " + port);
        System.out.printf("Backing up port: %d\n", backupPort);
        server.awaitTermination();

    }
}