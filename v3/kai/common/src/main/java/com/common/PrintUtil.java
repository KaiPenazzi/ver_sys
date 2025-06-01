package com.common;

import com.common.Config.Node;
import com.common.messages.LoggingMessage;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class PrintUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private static final int DATE_WIDTH = 20;
    private static final int NODE_WIDTH = 15;
    private static final int TYPE_WIDTH = 15;
    private static final int SUM_WIDTH = 10;

    public static void printLog(LoggingMessage msg) {
        var body = msg.body;
        String color = "\033[32m";

        switch (body.msg_type) {
            case "StartMessage":
            case "ResultMessage":
                color = "\033[34m";
                break;

            case "EchoMessage":
                color = "\033[33m";
                break;
        }

        Instant date = Instant.ofEpochSecond(body.timestamp);

        System.out.printf(color + "%-" + DATE_WIDTH + "s", formatter.format(date));
        System.out.print(" ");

        System.out.printf("%-" + NODE_WIDTH + "s", body.start_node);
        System.out.print(" → ");

        System.out.printf("%-" + NODE_WIDTH + "s", body.end_node);
        System.out.print(" ");

        System.out.printf("%-" + TYPE_WIDTH + "s", body.msg_type);
        System.out.print(" ");

        if (!msg.body.msg_type.equals("StartMessage")) {
            System.out.printf("%" + SUM_WIDTH + "d", body.sum);
        }
        System.out.println("\033[0m");
    }

    public static void printConfig(Config config) {
        var edges = edges(config);

        System.out.println("Nodes: " + config.nodes.size());
        System.out.println("Edges: " + edges.size());

        edges.forEach((edge) -> {
            System.out.println(edge);
        });
        System.out.println();

    }

    public static Set<String> edges(Config config) {
        Set pairs = new HashSet<String>();
        for (Node node : config.nodes) {
            for (String neighbor : node.neighbors) {
                pairs.add(makeKey(node.address, neighbor));
            }
        }

        return pairs;
    }

    private static String makeKey(String a, String b) {
        return a.compareTo(b) < 0 ? a + "|" + b : b + "|" + a;
    }
}
