package org.server;

import java.net.URI;
import java.util.List;

import org.example.BackupServiceGrpc;
import org.example.LoggedLog;
import org.example.BackupServiceGrpc.BackupServiceBlockingV2Stub;
import org.example.BackupServiceGrpc.BackupServiceStub;

import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

class BackupClient {

    ManagedChannel channel;
    BackupServiceStub async_stub;
    BackupServiceBlockingV2Stub sync_stub;

    StreamObserver<LoggedLog> addLogObserver;

    BackupClient(URI backup) {
        channel = io.grpc.ManagedChannelBuilder.forAddress(backup.getHost(), backup.getPort())
                .usePlaintext()
                .build();

        async_stub = BackupServiceGrpc.newStub(channel);
        sync_stub = BackupServiceGrpc.newBlockingV2Stub(channel);

        connectAddLog();
    }

    private void connectAddLog() {
        addLogObserver = async_stub.addLog(new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {
                System.out.println("Received log: " + value);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in add backup log: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    public List<LoggedLog> getLogs() {
        return sync_stub.getBackup(Empty.newBuilder().build()).getLogsList();
    }

    public void addLog(LoggedLog log) {
        addLogObserver.onNext(log);
    }

    public void stop() {
        addLogObserver.onCompleted();
        channel.shutdown();
    }
}
