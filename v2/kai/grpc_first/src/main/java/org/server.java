package org;

import org.example.LogServiceGrpc.LogServiceImplBase;
import org.example.LogServiceGrpc.LogServiceStub;

class Server extends LogServiceImplBase {
    // @Override
    // public void logMessage(LogRequest request, StreamObserver<LogResponse>
    // responseObserver) {
    // String message = request.getMessage();
    // System.out.println("Received message: " + message);
    //
    // LogResponse response = LogResponse.newBuilder()
    // .setStatus("Message logged successfully")
    // .build();
    //
    // responseObserver.onNext(response);
    // responseObserver.onCompleted();
    // }
}
