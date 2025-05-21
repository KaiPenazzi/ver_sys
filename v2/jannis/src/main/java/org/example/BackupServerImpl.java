package org.example;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.BackupServiceGrpc.BackupServiceImplBase;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackupServerImpl extends BackupServiceImplBase
{
    private List<LoggedLog> log_list = Collections.synchronizedList(new ArrayList<LoggedLog>());

    @Override
    public StreamObserver<LoggedLog> addLog(StreamObserver<Empty> responseObserver) {

        return new StreamObserver<LoggedLog>() {

            @Override
            public void onNext(LoggedLog log) {
                log_list.add(log);
                printLog(log);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getBackup(Empty request, StreamObserver<ListLoggedLog> responseObserver) {
        ListLoggedLog list = ListLoggedLog.newBuilder()
                .addAllLogs(log_list)
                .build();
        responseObserver.onNext(list);
        responseObserver.onCompleted();
    }

    public static void printLog(LoggedLog log) {
        Instant instant = Instant.ofEpochSecond(log.getTimestamp().getSeconds());
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());

        String datumFormatiert = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(zdt);
        System.out.println(log.getLineNumber() + "\t" + datumFormatiert + "\t\t" + log.getLog().getUsrId() + "\t " + log.getLog().getLogText());

    }
}

