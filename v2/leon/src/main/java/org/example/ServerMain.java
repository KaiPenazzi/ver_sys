package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;


public class ServerMain{
    public static void main(String[] args) throws Exception{
        int port = 2222;

        Server server = ServerBuilder.forPort(port)
                                            .addService(new myServer())
                                            .build();
        server.start();
        System.out.println("Server is running");
        server.awaitTermination();

    }
}