package org.backup;

import org.example.BackupServiceGrpc.BackupServiceImplBase;

import java.util.ArrayList;
import java.util.List;

import org.example.LoggedLog;
import org.example.ListLoggedLog;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.utils.Printer;

class MyService extends BackupServiceImplBase {
    List<LoggedLog> loggedLogs = new ArrayList<LoggedLog>();

    @Override
    public StreamObserver<LoggedLog> addLog(StreamObserver<Empty> responseObserver) {
        return new StreamObserver<LoggedLog>() {
            @Override
            public void onNext(LoggedLog value) {
                Printer.loggedlog(value);
                loggedLogs.add(value);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getBackup(Empty request, StreamObserver<ListLoggedLog> responseObserver) {
        ListLoggedLog.Builder builder = ListLoggedLog.newBuilder();
        for (LoggedLog log : loggedLogs) {
            builder.addLogs(log);
        }
        ListLoggedLog listLoggedLog = builder.build();
        responseObserver.onNext(listLoggedLog);
        responseObserver.onCompleted();
    }

}
