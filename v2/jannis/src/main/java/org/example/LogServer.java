package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class LogServer {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 2222)
                .usePlaintext()
                .build();
        BackupServiceGrpc.BackupServiceStub stub = BackupServiceGrpc.newStub(channel);
        BackupServiceGrpc.BackupServiceBlockingStub blockingStub = BackupServiceGrpc.newBlockingStub(channel);


        Server server = ServerBuilder.forPort(1111)
                .addService(new LogServiceImpl(stub, blockingStub, "1234"))
                .build();

        System.out.println("Server läuft auf Port 1111...");
        server.start();
        server.awaitTermination();
    }
}
