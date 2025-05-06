package org.example;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class myClient {

    private String userId;
    private LogServiceGrpc.LogServiceBlockingStub  myBlockingStub;
    private LogServiceGrpc.LogServiceStub myAsyncStub;

    public myClient(String userId, LogServiceGrpc.LogServiceBlockingStub blockingStub, LogServiceGrpc.LogServiceStub asyncStub){
        this.userId = userId;
        this.myBlockingStub = blockingStub;
        this.myAsyncStub = asyncStub;
    }

    public void addLog(String log){
        StreamObserver<Empty> responseObserver = new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {

            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error Client addLog: "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        };
       StreamObserver<Log> requestObserver =  myAsyncStub.addLog(responseObserver);

       requestObserver.onNext(Log.newBuilder().setUsrId(userId).setLogText(log).build());

       requestObserver.onCompleted();

    }

    public void getLog(){
        ListLoggedLog loggedLogs = myBlockingStub.getLog(Empty.newBuilder().build());
            for(LoggedLog l : loggedLogs.getLogsList()){
                System.out.println(myClient.logToString(l));
            }
    }

    public void listen(String userId){
        User u = User.newBuilder().setUserId(userId).build();

        myAsyncStub.listenLog(u, new StreamObserver<LoggedLog>() {
            @Override
            public void onNext(LoggedLog loggedLog) {
                System.out.println(myClient.logToString(loggedLog));
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error Client listen: "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        });
    }
    public void unlisten(String userId){
        User u = User.newBuilder().setUserId(userId).build();
        myBlockingStub.unlistenLog(u);
    }

    public static String logToString(LoggedLog log){

        String timestampString = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(java.time.ZoneId.systemDefault())
                .format(java.time.Instant.ofEpochSecond(log.getTimestamp().getSeconds()));
        String logString =
                log.getLineNumber() + " / " + timestampString + " / " + log.getLog().getUsrId() + " / " + log.getLog().getLogText();
        return logString;
    }
    public String getUserId(){
        return this.userId;
    }
}
