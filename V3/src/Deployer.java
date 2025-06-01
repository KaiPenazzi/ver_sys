import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Deployer {

    private final List<Node> nodes;
    private final String loggerAddress;
    private final String jarPath;
    private final List<Process> startedProcesses = new ArrayList<>();

    public Deployer(List<Node> nodes, String loggerAddress, String jarPath) {
        this.nodes = nodes;
        this.loggerAddress = loggerAddress;
        this.jarPath = jarPath;

        // Shutdown-Hook registrieren
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown detected. Stopping all node processes...");
            stopAllProcesses();
        }));
    }

    public void deploy() {
        for (Node node : nodes) {
            startNodeProcess(node);
        }
    }

    private void startNodeProcess(Node node) {
        String self = node.getIp() + ":" + node.getPort();
        String storage = String.valueOf(node.getSum());

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(jarPath);
        command.add(storage);
        command.add(self);
        command.add(loggerAddress);
        command.addAll(node.getNeighbours());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();

        try {
            Process process = pb.start();
            startedProcesses.add(process);


        } catch (IOException e) {
            System.err.println("Fehler beim Starten von Node: " + self);
            e.printStackTrace();
        }
    }

    public void stopAllProcesses() {
        for (Process process : startedProcesses) {
            if (process.isAlive()) {
                System.out.println("Killing process...");
                process.destroy();
            }
        }
        startedProcesses.clear();
    }
}
