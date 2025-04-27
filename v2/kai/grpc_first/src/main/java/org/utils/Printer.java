package org.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.example.ListLoggedLog;
import org.example.LoggedLog;

public class Printer {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private static final int LINE_NUMBER_WIDTH = 10;
    private static final int USER_ID_WIDTH = 15;
    private static final int LOG_TEXT_WIDTH = 30;
    private static final int DATE_WIDTH = 20;

    public static void loggedlog(LoggedLog log) {
        // Formatierte Ausgabe für die Log-Daten
        System.out.printf("%-" + LINE_NUMBER_WIDTH + "s", log.getLineNumber());
        System.out.print(" ");

        System.out.printf("%-" + USER_ID_WIDTH + "s", log.getLog().getUsrId());
        System.out.print(" ");

        System.out.printf("%-" + LOG_TEXT_WIDTH + "s", log.getLog().getLogText());
        System.out.print(" ");

        Instant date = Instant.ofEpochSecond(log.getTimestamp().getSeconds());
        System.out.printf("%-" + DATE_WIDTH + "s", formatter.format(date));
        System.out.println();
    }

    public static void listloggedlog(ListLoggedLog listloggedlog) {
        header();

        for (LoggedLog log : listloggedlog.getLogsList()) {
            loggedlog(log);
        }
    }

    public static void header() {
        System.out.printf("%-" + LINE_NUMBER_WIDTH + "s", "Line");
        System.out.print(" ");

        System.out.printf("%-" + USER_ID_WIDTH + "s", "UserId");
        System.out.print(" ");

        System.out.printf("%-" + LOG_TEXT_WIDTH + "s", "LogText");
        System.out.print(" ");

        System.out.printf("%-" + DATE_WIDTH + "s", "Date");
        System.out.println();

        System.out.println("-".repeat(LINE_NUMBER_WIDTH + USER_ID_WIDTH + LOG_TEXT_WIDTH + DATE_WIDTH + 6));
    }
}
