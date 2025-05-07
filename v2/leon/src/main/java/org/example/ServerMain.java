package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;


public class ServerMain{
    public static void main(String[] args) throws Exception{
        int port = 2222;
        int backupPort = 3333;
        Server server = ServerBuilder.forPort(port)
                                            .addService(new myServer())
                                            .build();


        server.start();

//        Server backupServer = ServerBuilder.forPort(backupPort).addService(new myBackupServer()).build();
//        backupServer.start();
        System.out.println("Server is running on port " + port);
        System.out.printf("Backing up port: %d\n", backupPort);
        server.awaitTermination();

    }
}