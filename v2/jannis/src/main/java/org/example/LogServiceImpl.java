package org.example;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.LogServiceGrpc.LogServiceImplBase;
import com.google.protobuf.util.Timestamps;

import java.util.*;

public class LogServiceImpl extends LogServiceImplBase
{
    private List<LoggedLog> log_list = Collections.synchronizedList(new ArrayList<LoggedLog>());
    private Map<String, StreamObserver<LoggedLog>> listener_map = Collections.synchronizedMap(new HashMap<String, StreamObserver<LoggedLog>>());

    private int linecounter = 1;

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
                        .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                        .build();

                log_list.add(logged);

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
}
