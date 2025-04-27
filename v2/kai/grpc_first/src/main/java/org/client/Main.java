package org.client;

import org.example.ListLoggedLog;
import org.example.Log;
import org.example.LogServiceGrpc;
import org.example.LogServiceGrpc.LogServiceBlockingV2Stub;
import org.example.LogServiceGrpc.LogServiceStub;
import org.utils.Printer;

import com.google.protobuf.Empty;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

public class Main {
    public static void main(String[] args) {
        System.out.println("run client");

        ManagedChannel channel = io.grpc.ManagedChannelBuilder.forAddress("127.0.0.1", 3000)
                .usePlaintext()
                .build();

        LogServiceStub async_stub = LogServiceGrpc.newStub(channel);
        LogServiceBlockingV2Stub sync_stub = LogServiceGrpc.newBlockingV2Stub(channel);

        Log log = Log.newBuilder()
                .setUsrId("123")
                .setLogText("test log")
                .build();

        StreamObserver<Empty> send_observer = new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {
                System.out.println("Log added successfully");
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed");
            }
        };

        StreamObserver<Log> add_observer = async_stub.addLog(send_observer);

        System.out.println("Sending log...");
        add_observer.onNext(log);
        add_observer.onNext(log);
        System.out.println("Completing stream...");
        add_observer.onCompleted();

        ListLoggedLog logs = sync_stub.getLog(Empty.getDefaultInstance());
        Printer.listloggedlog(logs);

        channel.shutdown();

        try {
            channel.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Channel termination interrupted: " + e.getMessage());
        }
    }
}
