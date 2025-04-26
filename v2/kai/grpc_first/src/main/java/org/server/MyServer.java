package org.server;

import org.example.Log;
import org.example.LogServiceGrpc.LogServiceImplBase;

import com.google.protobuf.Empty;

import io.grpc.stub.StreamObserver;

class MyServer extends LogServiceImplBase {
    @Override
    public StreamObserver<Log> addLog(StreamObserver<Empty> responseObserver) {
        System.out.println("Received log request");

        return new StreamObserver<Log>() {
            @Override
            public void onNext(Log value) {
                System.out.println("Received log: " + value.getLogText());
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
}
