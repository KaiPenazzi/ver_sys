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

        // 3. Latch für Synchronisation (damit main nicht vorzeitig endet)
        CountDownLatch finishLatch = new CountDownLatch(1);

        // 4. Antwort-Observer für AddLog (für die serverseitige Antwort: Empty)
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

        // 5. Request-Stream an den Server senden (AddLog)
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

            // Stream abschließen (nichts mehr senden)
            requestObserver.onCompleted();

        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }

        // 6. Auf Abschluss warten
        if (!finishLatch.await(5, TimeUnit.SECONDS)) {
            System.err.println("⚠️ Timeout beim Warten auf Serverantwort");
        }

        // 7. Test für GetLog hinzufügen
        getLogs(stub);

        // 8. Verbindung schließen
        channel.shutdownNow();
    }

    // Methode, die die `GetLog`-Methode des Servers testet
    private static void getLogs(LogServiceGrpc.LogServiceStub stub) throws InterruptedException {
        System.out.println("\n🔄 Holen der Logs...");

        // CountDownLatch für Synchronisation
        CountDownLatch finishLatch = new CountDownLatch(1);

        // Antwort-Observer für GetLog
        StreamObserver<ListLoggedLog> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ListLoggedLog listLoggedLog) {
                System.out.println("✅ Logs empfangen:");
                listLoggedLog.getLogsList().forEach(log -> {
                    System.out.println("Zeilennummer: " + log.getLineNumber() + ", Log: " + log.getLog().getLogText() + ", User: " + log.getLog().getUsrId());
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

        // Sende leeren Request (Google Empty) für GetLog
        stub.getLog(Empty.getDefaultInstance(), responseObserver);

        // Warten auf Antwort
        if (!finishLatch.await(5, TimeUnit.SECONDS)) {
            System.err.println("⚠️ Timeout beim Warten auf Serverantwort");
        }
    }
}
