package org.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.ListLoggedLog;
import org.example.Log;
import org.example.LoggedLog;
import org.example.User;
import org.example.LogServiceGrpc.LogServiceImplBase;

import com.google.protobuf.Empty;

import io.grpc.stub.StreamObserver;

class MyService extends LogServiceImplBase {
    private Logger logger = new Logger();
    private Map<String, StreamObserver<LoggedLog>> listeners = new HashMap<String, StreamObserver<LoggedLog>>();

    @Override
    public StreamObserver<Log> addLog(StreamObserver<Empty> responseObserver) {
        System.out.println("Received log request");

        return new StreamObserver<Log>() {
            @Override
            public void onNext(Log value) {
                LoggedLog loggedlog = logger.addLog(value);

                listeners.forEach((usr, observer) -> {
                    System.out.println("Sending log to listener: " + usr);
                    observer.onNext(loggedlog);
                });
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("REC addLog ERROR: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed");
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getLog(Empty request, StreamObserver<ListLoggedLog> responseObserver) {
        System.out.println("Received getLogs request");
        responseObserver.onNext(logger.getLogs());
        responseObserver.onCompleted();
    }

    @Override
    public void listenLog(User request, StreamObserver<LoggedLog> responseObserver) {
        System.out.println("Received listenLog request");
        listeners.put(request.getUserId(), responseObserver);
    }

    @Override
    public void unlistenLog(User request, StreamObserver<Empty> responseObserver) {
        System.out.println("Received stopListenLog request");

        var listenerObserver = listeners.remove(request.getUserId());
        listenerObserver.onCompleted();

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
