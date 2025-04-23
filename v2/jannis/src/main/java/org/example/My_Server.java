package org.example;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class My_Server extends LogServiceGrpc.LogServiceImplBase
{
    @Override
    public StreamObserver<LogServiceOuterClass.Log> addLog(StreamObserver<Empty> responseObserver) {
        return super.addLog(responseObserver);
    }
}
