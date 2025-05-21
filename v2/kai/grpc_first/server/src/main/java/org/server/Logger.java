package org.server;

import java.util.ArrayList;
import java.util.List;

import org.example.ListLoggedLog;
import org.example.Log;
import org.example.LoggedLog;
import org.utils.Printer;

import com.google.protobuf.Timestamp;

class Logger {
    private List<LoggedLog> logs = new ArrayList<LoggedLog>();

    public LoggedLog addLog(Log log) {
        LoggedLog loggedLog = LoggedLog.newBuilder()
                .setLog(log)
                .setTimestamp(Timestamp.newBuilder().setSeconds(java.time.Instant.now().getEpochSecond()))
                .setLineNumber(logs.size())
                .build();

        Printer.loggedlog(loggedLog);

        logs.add(loggedLog);
        return loggedLog;
    }

    public ListLoggedLog getLogs() {
        var builder = ListLoggedLog.newBuilder();

        for (LoggedLog log : this.logs) {
            builder.addLogs(log);
        }
        return builder.build();
    }

    public void crash() {
        logs.clear();
    }

    public void setLogs(List<LoggedLog> logs) {
        this.logs = logs;
    }
}
