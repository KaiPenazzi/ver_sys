package org.example;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LogClient {

    public static void main(String[] args) throws InterruptedException {
        // 1. Kanal zum Server öffnen
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // 2. Stub für asynchrone gRPC-Aufrufe
        LogServiceGrpc.LogServiceStub stub = LogServiceGrpc.newStub(channel);

        // 3. Latch für Synchronisation
        CountDownLatch finishLatch = new CountDownLatch(1);

        // 4. Listener starten (vor AddLog)
        listenLogs(stub);

        // 5. Antwort-Observer für AddLog
        StreamObserver<Empty> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(Empty empty) {
                System.out.println("✅ Server hat alle Logs erhalten.");
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Fehler bei AddLog: " + t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("🔚 AddLog-Stream abgeschlossen.");
                finishLatch.countDown();
            }
        };

        // 6. Request-Stream an den Server senden (AddLog)
        StreamObserver<Log> requestObserver = stub.addLog(responseObserver);

        try {
            // Beispiel-Logs senden
            requestObserver.onNext(Log.newBuilder()
                    .setUsrId("user123")
                    .setLogText("Erster Logeintrag")
                    .build());

            requestObserver.onNext(Log.newBuilder()
                    .setUsrId("user456")
                    .setLogText("Zweiter Logeintrag")
                    .build());

            requestObserver.onNext(Log.newBuilder()
                    .setUsrId("user789")
                    .setLogText("Dritter Logeintrag")
                    .build());

            requestObserver.onCompleted();

        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }

        // 7. Auf Abschluss von AddLog warten
        if (!finishLatch.await(5, TimeUnit.SECONDS)) {
            System.err.println("⚠️ Timeout beim Warten auf Serverantwort");
        }

        // 8. GetLog-Test (auskommentiert – bei Bedarf aktivieren)
        // getLogs(stub);

        // 9. Verbindung schließen (nach kurzer Wartezeit, um evtl. Logs zu empfangen)
        Thread.sleep(2000);
        channel.shutdownNow();
    }

    // Neuer Test für ListenLog
    private static void listenLogs(LogServiceGrpc.LogServiceStub stub) {
        System.out.println("📡 Warte auf neue Logs vom Server...");

        StreamObserver<LoggedLog> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(LoggedLog loggedLog) {
                System.out.println("📥 Neuer Log-Eintrag empfangen:");
                System.out.println("  Zeile: " + loggedLog.getLineNumber());
                System.out.println("  Zeit:  " + loggedLog.getTimestamp());
                System.out.println("  User:  " + loggedLog.getLog().getUsrId());
                System.out.println("  Text:  " + loggedLog.getLog().getLogText());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Fehler beim Empfangen von Logs: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("🔚 ListenLog beendet.");
            }
        };

        stub.listenLog(User.getDefaultInstance(), responseObserver);
    }

    // Bestehender Test für GetLog (bleibt erhalten)
    private static void getLogs(LogServiceGrpc.LogServiceStub stub) throws InterruptedException {
        System.out.println("\n🔄 Holen der Logs...");

        CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<ListLoggedLog> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ListLoggedLog listLoggedLog) {
                System.out.println("✅ Logs empfangen:");
                listLoggedLog.getLogsList().forEach(log -> {
                    System.out.println("Zeilennummer: " + log.getLineNumber() +
                            ", Timestamp: " + log.getTimestamp() +
                            ", Log: " + log.getLog().getLogText() +
                            ", User: " + log.getLog().getUsrId());
                });
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Fehler bei GetLog: " + t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("🔚 GetLog abgeschlossen.");
                finishLatch.countDown();
            }
        };

        stub.getLog(Empty.getDefaultInstance(), responseObserver);

        if (!finishLatch.await(5, TimeUnit.SECONDS)) {
            System.err.println("⚠️ Timeout beim Warten auf Serverantwort");
        }
    }
}
