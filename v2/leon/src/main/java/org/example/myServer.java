package org.example;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.example.LogServiceGrpc.LogServiceImplBase;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;

import java.util.*;


/*
 * diese Klasse implementiert den Logservice Serverseitig
 */
public class myServer extends LogServiceImplBase {

    private int line = 1;
    private final String PASSWORD = "password";
    private List<LoggedLog> loggedLogs = Collections.synchronizedList(new ArrayList<>());
    private Map<String,StreamObserver<LoggedLog>> listeners = Collections.synchronizedMap(new HashMap<String,StreamObserver<LoggedLog>>());
    private BackupServiceGrpc.BackupServiceBlockingStub  BackupBlockingStub;
    private BackupServiceGrpc.BackupServiceStub BackupAsyncStub;


    public myServer() {

        Channel backupChannel = ManagedChannelBuilder.forAddress("127.0.0.1", 3333)
                .usePlaintext()
                .build();

        BackupAsyncStub = BackupServiceGrpc.newStub(backupChannel);
        BackupBlockingStub = BackupServiceGrpc.newBlockingStub(backupChannel);
    }


    /**
     * fügt übergebenen Log zu LogListe hinzu
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<Log> addLog(StreamObserver<Empty> responseObserver)
    {
        return new StreamObserver<Log>() {

            StreamObserver<LoggedLog> BackupStreamObserver = BackupAsyncStub.addLog(new StreamObserver<Empty>() {

                @Override
                public void onNext(Empty value) {

                }
                @Override
                public void onError(Throwable throwable){
                }
                @Override
                public void onCompleted() {
                    System.out.println("Backup Stream completed");
                }
            });



            @Override
            public void onNext(Log log) {

                long seconds = java.time.Instant.now().getEpochSecond();
                Timestamp timestamp = Timestamp.newBuilder().setSeconds(seconds).build();
                LoggedLog loggedLog = LoggedLog.newBuilder().setLog(log)
                                .setLineNumber(line++)
                                .setTimestamp(timestamp)
                                .build();
                //lokal
                loggedLogs.add(loggedLog);

                BackupStreamObserver.onNext(loggedLog);


                //Listener
                System.out.println("Log: " + loggedLog.getLog().getLogText() + " added;");
                for( StreamObserver<LoggedLog> l : listeners.values() ){
                    l.onNext(loggedLog);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("a Client has stopped the Stream");
                try {
                    BackupStreamObserver.onCompleted();
                }
                catch ( Exception e){

                }
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * gibt alle Logs als Liste zurück
     * @param request
     * @param responseObserver
     */
    @Override
    public void getLog(Empty request, StreamObserver<ListLoggedLog> responseObserver)
    {
        ListLoggedLog.Builder logListBuilder = ListLoggedLog.newBuilder();
        logListBuilder.addAllLogs(loggedLogs);

        responseObserver.onNext(logListBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void listenLog(User request, StreamObserver<LoggedLog> responseObserver) {
        //User zu den listenern hinzufügen
        listeners.put(request.getUserId(), responseObserver);
        System.out.println("User: "+ request.getUserId() + " is listening");
    }


    @Override
    public void unlistenLog(User request, StreamObserver<Empty> responseObserver) {
        //user von den listenern streichen und Stream schließen
        listeners.remove(request.getUserId());
        System.out.println("User: "+ request.getUserId() + " is not listening anymore" );
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }

    @Override
    public void crashLog(Password request, StreamObserver<Empty> responseObserver) {
        if(request.getPsw().equals(PASSWORD)){
            //Liste leeren
            loggedLogs.clear();
            System.out.println("server cleared");
        }
        else{
            System.out.println("wrong password");
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }
    @Override
    public void restoreLog(Password request, StreamObserver<Empty> responseObserver) {
        if(request.getPsw().equals(PASSWORD)){
            try {
                ListLoggedLog backupLogs = BackupBlockingStub.getBackup(Empty.getDefaultInstance());

                synchronized (loggedLogs){
                    loggedLogs.clear();
                    loggedLogs.addAll(backupLogs.getLogsList());
                }
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }else {
            System.out.println("wrong password");
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }



}


