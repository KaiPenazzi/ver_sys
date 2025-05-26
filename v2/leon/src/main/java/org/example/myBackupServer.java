package org.example;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.BackupServiceGrpc.BackupServiceImplBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class myBackupServer extends BackupServiceImplBase {

    private List<LoggedLog> loggedLogs = Collections.synchronizedList(new ArrayList<>());


    @Override
    public StreamObserver<LoggedLog> addLog(StreamObserver<Empty> responseObserver){
        return new StreamObserver<LoggedLog>() {
            @Override
            public void onNext(LoggedLog loggedLog) {
                loggedLogs.add(loggedLog);
                System.out.println(myClient.logToString(loggedLog));

            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };

    }
    @Override
    public void getBackup(Empty request, StreamObserver<ListLoggedLog> responseObserver){
        ListLoggedLog.Builder logListBuilder = ListLoggedLog.newBuilder();
        logListBuilder.addAllLogs(loggedLogs);
        responseObserver.onNext(logListBuilder.build());
        responseObserver.onCompleted();
    }


}
