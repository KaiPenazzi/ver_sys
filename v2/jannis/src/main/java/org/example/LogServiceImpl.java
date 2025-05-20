package org.example;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.example.LogServiceGrpc.LogServiceImplBase;

import java.util.*;

public class LogServiceImpl extends LogServiceImplBase
{
    private List<LoggedLog> log_list = Collections.synchronizedList(new ArrayList<LoggedLog>());
    private Map<String, StreamObserver<LoggedLog>> listener_map = Collections.synchronizedMap(new HashMap<String, StreamObserver<LoggedLog>>());
    private int linecounter = 1;
    String password;

    private final BackupServiceGrpc.BackupServiceStub backupServiceStub;
    private final BackupServiceGrpc.BackupServiceBlockingStub backupServiceBlockingStub;
    private final StreamObserver<LoggedLog> backupStream;

    public LogServiceImpl (BackupServiceGrpc.BackupServiceStub backupServiceStub, BackupServiceGrpc.BackupServiceBlockingStub blockingStub,  String password)
    {
        this.backupServiceStub = backupServiceStub;
        this.backupServiceBlockingStub = blockingStub;
        this.password = password;
        this.backupStream = backupServiceStub.addLog(new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {
                System.out.println("Backup server acknowledged logs.");
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in backup stream: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Backup stream completed.");
            }
        });

    }

    @Override
    public StreamObserver<Log> addLog(StreamObserver<Empty> responseObserver)
    {
        return new StreamObserver<Log>() {

            @Override
            public void onNext(Log log) {
                System.out.println("Log empfang: " + log);
                LoggedLog logged = LoggedLog.newBuilder()
                        .setLog(log)
                        .setLineNumber(linecounter++)
                        .setTimestamp(Timestamp.newBuilder().setSeconds(java.time.Instant.now().getEpochSecond()))
                        .build();

                log_list.add(logged);
                backupStream.onNext(logged);

                listener_map.forEach( (k,v) -> {
                    v.onNext(logged);
                });
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("Log empfang completed");

                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getLog(Empty request, StreamObserver<ListLoggedLog> responseObserver) {

        ListLoggedLog.Builder builder = ListLoggedLog.newBuilder();
        builder.addAllLogs(log_list);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void listenLog(User request, StreamObserver<LoggedLog> responseObserver) {
        listener_map.put(request.getUserId(), responseObserver);
    }

    @Override
    public void unlistenLog(User request, StreamObserver<Empty> responseObserver) {
        listener_map.remove(request.getUserId()).onCompleted();
    }

    @Override
    public void crashLog(Password request, StreamObserver<Empty> responseObserver) {
        if (request.getPsw().equals(password)) {
            log_list.clear();
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void restoreLog(Password request, StreamObserver<Empty> responseObserver) {
        if (!request.getPsw().equals(password)) {
            return;
        }

        ListLoggedLog backup = backupServiceBlockingStub.getBackup(Empty.newBuilder().build());
        log_list.clear();
        log_list.addAll(backup.getLogsList());

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
