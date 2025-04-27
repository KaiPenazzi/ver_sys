package org.client;

import java.net.URI;
import java.net.URL;

import org.example.Log;
import org.example.LogServiceGrpc;
import org.example.LoggedLog;
import org.example.User;
import org.example.LogServiceGrpc.LogServiceBlockingV2Stub;
import org.example.LogServiceGrpc.LogServiceStub;
import org.utils.Printer;

import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

public class MyClient {
    ManagedChannel channel;
    LogServiceStub async_stub;
    LogServiceBlockingV2Stub sync_stub;
    String usr;
    URI url;

    StreamObserver<Log> addLogObserver;

    public MyClient(String usr, URI url) {
        this.usr = usr;
        this.url = url;
    }

    public void start() {
        System.out.println("Client started");

        channel = io.grpc.ManagedChannelBuilder.forAddress(url.getHost(), url.getPort())
                .usePlaintext()
                .build();

        async_stub = LogServiceGrpc.newStub(channel);
        sync_stub = LogServiceGrpc.newBlockingV2Stub(channel);

        connectAddLog();
    }

    public void stop() {
        disconnectAddLog();
        channel.shutdown();

        try {
            channel.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Channel termination interrupted: " + e.getMessage());
        }
    }

    public void addLog(String Message) {
        addLogObserver.onNext(Log.newBuilder()
                .setUsrId(usr)
                .setLogText(Message)
                .build());
    }

    private void connectAddLog() {
        addLogObserver = async_stub.addLog(new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {
                System.out.println("Log added successfully");
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: could not add log: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("AddLog Stream completed");
            }
        });

    }

    private void disconnectAddLog() {
        addLogObserver.onCompleted();
    }

    public void getLog() {
        var logs = sync_stub.getLog(Empty.getDefaultInstance());
        Printer.listloggedlog(logs);
    }

    public void listenLog() {
        Printer.header();
        async_stub.listenLog(User.newBuilder().setUserId(usr).build(), new StreamObserver<LoggedLog>() {
            @Override
            public void onNext(org.example.LoggedLog value) {
                Printer.loggedlog(value);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: could not listen log: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("ListenLog Stream completed");
            }
        });
    }

    public void unlistenLog() {
        async_stub.unlistenLog(User.newBuilder().setUserId(usr).build(), new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {
                System.out.println("UnlistenLog completed");
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: could not unlisten log: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("UnlistenLog Stream completed");
            }
        });
    }
}
