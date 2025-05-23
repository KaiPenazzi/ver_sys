package org.server;

import java.util.HashMap;
import java.util.Map;

import org.example.ListLoggedLog;
import org.example.Log;
import org.example.LoggedLog;
import org.example.Password;
import org.example.User;
import org.example.LogServiceGrpc.LogServiceImplBase;

import com.google.protobuf.Empty;

import io.grpc.stub.StreamObserver;

class MyService extends LogServiceImplBase {
    private Logger logger = new Logger();
    private Map<String, StreamObserver<LoggedLog>> listeners = new HashMap<String, StreamObserver<LoggedLog>>();
    private BackupClient backup;

    public MyService(BackupClient backup) {
        this.backup = backup;
    }

    @Override
    public StreamObserver<Log> addLog(StreamObserver<Empty> responseObserver) {
        return new StreamObserver<Log>() {
            @Override
            public void onNext(Log value) {
                LoggedLog loggedlog = logger.addLog(value);
                backup.addLog(loggedlog);

                listeners.forEach((usr, observer) -> {
                    observer.onNext(loggedlog);
                });
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("REC addLog ERROR: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getLog(Empty request, StreamObserver<ListLoggedLog> responseObserver) {
        responseObserver.onNext(logger.getLogs());
        responseObserver.onCompleted();
    }

    @Override
    public void listenLog(User request, StreamObserver<LoggedLog> responseObserver) {
        listeners.put(request.getUserId(), responseObserver);
    }

    @Override
    public void unlistenLog(User request, StreamObserver<Empty> responseObserver) {
        var listenerObserver = listeners.remove(request.getUserId());
        listenerObserver.onCompleted();

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void crashLog(Password request, StreamObserver<Empty> responseObserver) {
        if (request.getPsw().equals("1234")) {
            logger.crash();
        } else {
            System.err.println("Invalid password");
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void restoreLog(Password request, StreamObserver<Empty> responseObserver) {
        if (request.getPsw().equals("1234")) {
            logger.setLogs(backup.getLogs());
        } else {
            System.err.println("Invalid password");
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
