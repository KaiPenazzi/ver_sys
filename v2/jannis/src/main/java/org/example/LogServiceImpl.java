package org.example;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.LogServiceGrpc.LogServiceImplBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogServiceImpl extends LogServiceImplBase
{
    private List<LoggedLog> log_list = Collections.synchronizedList(new ArrayList<LoggedLog>());
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
                        .build();

                log_list.add(logged);
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
}
