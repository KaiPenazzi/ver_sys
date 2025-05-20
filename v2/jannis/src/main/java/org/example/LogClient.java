package org.example;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LogClient {

    String username;
    LogServiceGrpc.LogServiceBlockingStub blockingStubb;
    LogServiceGrpc.LogServiceStub asyncStubb;
    LogServiceGrpc.LogServiceBlockingStub BlockingStubb;
    StreamObserver<LoggedLog> activeListenObserver = null;


    public LogClient(String username, LogServiceGrpc.LogServiceBlockingStub blockingStubb, LogServiceGrpc.LogServiceStub asyncStubb) {
        this.username = username;
        this.blockingStubb = blockingStubb;
        this.asyncStubb = asyncStubb;
    }

    public void addLog(String log) {
        StreamObserver<Empty> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(Empty empty) {
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Fehler bei AddLog: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        };
        StreamObserver<Log> addStreamRequestObserver = asyncStubb.addLog(responseObserver);

        addStreamRequestObserver.onNext(Log.newBuilder()
                .setUsrId(username)
                .setLogText(log)
                .build());

        addStreamRequestObserver.onCompleted();
    }

    public void getLog() {

        ListLoggedLog logs = blockingStubb.getLog(Empty.getDefaultInstance());

        for (LoggedLog log : logs.getLogsList()) {
            printLog(log);
        }
    }

    public void listen() {
        activeListenObserver = new StreamObserver<>() {
            @Override
            public void onNext(LoggedLog loggedLog) {
                printLog(loggedLog);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Fehler beim Empfangen von Logs: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("ListenLog beendet.\n$");
            }
        };

        asyncStubb.listenLog(User.newBuilder()
                        .setUserId(username)
                        .build()
                , activeListenObserver);
    }

    public void unlisten() {
        StreamObserver<Empty> unlistenResponseObserver = new StreamObserver<>() {
            @Override
            public void onNext(Empty empty) {
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Fehler beim Abmelden: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        };

        asyncStubb.unlistenLog(User.newBuilder().setUserId(username).build(), unlistenResponseObserver);
        activeListenObserver = null;
    }

    public void crashLog()
    {
        StreamObserver<Empty> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(Empty empty) {
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Fehler beim Abmelden: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        };

        Scanner scanner = new Scanner(System.in);
        System.out.println("Gebe das Passwort ein um die Logs zu löschen:");
        String password = scanner.nextLine();

        asyncStubb.crashLog(Password.newBuilder()
                .setPsw(password)
                .build(), responseObserver);
    }

    public void restoreLog()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Gib das Passwort ein, um die Logs wiederherzustellen:");
        String password = scanner.nextLine();

        try {
            blockingStubb.restoreLog(Password.newBuilder()
                    .setPsw(password)
                    .build());
            System.out.println("✅ Logs wurden erfolgreich wiederhergestellt.");
        } catch (Exception e) {
            System.err.println("❌ Fehler beim Wiederherstellen der Logs: " + e.getMessage());
        }
    }

    public static void printLog(LoggedLog log) {
        Instant instant = Instant.ofEpochSecond(log.getTimestamp().getSeconds());
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());

        String datumFormatiert = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(zdt);
        System.out.println(log.getLineNumber() + "\t" + datumFormatiert + "\t\t" + log.getLog().getUsrId() + "\t " + log.getLog().getLogText());

    }
}