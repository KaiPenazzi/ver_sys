import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.Scanner;

public class TicTacToeGUI {
    public static TicTacToeGUI instance;

    private JButton neuesSpielButton;
    private JButton joinButton;
    private JPanel spielfeld;
    private JTextField width;
    private JTextField height;
    private JTextField value;
    private JFrame frame;
    private int winCondition = 3;

    private JButton[][] buttons; // Matrix zur Verwaltung der Spielfelder

    public TicTacToeGUI() {
        instance = this;
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        // Steuer-Panel mit Eingabefeldern und Buttons
        JPanel controlPanel = new JPanel();
        width = new JTextField("3", 3);
        height = new JTextField("3", 3);
        value = new JTextField("3", 3);

        neuesSpielButton = new JButton("Neues Spiel");
        joinButton = new JButton("Join");

        controlPanel.add(new JLabel("Breite:"));
        controlPanel.add(width);
        controlPanel.add(new JLabel("Höhe:"));
        controlPanel.add(height);
        controlPanel.add(new JLabel("Punkte für Sieg:"));
        controlPanel.add(value);
        controlPanel.add(neuesSpielButton);
        controlPanel.add(joinButton);

        spielfeld = new JPanel();
        spielfeld.setLayout(new GridLayout(3, 3)); // Standardgröße

        neuesSpielButton.addActionListener(e -> {
            updateWinCondition();
            erstelleSpielfeld(); // Nur leere Felder erzeugen
            try {
                Spiellogik.start_new_Game(
                        Integer.parseInt(width.getText()),
                        Integer.parseInt(height.getText()),
                        Integer.parseInt(value.getText()),
                        true
                );
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
        });

        joinButton.addActionListener(e -> {
            try {
                onJoinClicked();
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
        });

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(spielfeld, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void erstelleSpielfeld() {
        try {
            int w = Integer.parseInt(width.getText());
            int h = Integer.parseInt(height.getText());

            spielfeld.removeAll();
            spielfeld.setLayout(new GridLayout(h, w));
            buttons = new JButton[h][w];

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    JButton feld = new JButton("");
                    feld.setFont(new Font("Arial", Font.PLAIN, 40));
                    feld.setFocusPainted(false);
                    feld.setPreferredSize(new Dimension(80, 80));

                    final int row = i;
                    final int col = j;
                    feld.addActionListener(e -> {
                        try {
                            onFieldClicked(row, col, feld);
                        } catch (SocketException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    buttons[i][j] = feld;
                    spielfeld.add(feld);
                }
            }

            spielfeld.revalidate();
            spielfeld.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Bitte gültige Zahlen für Breite und Höhe eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setzeFeldMitDaten(String[][] daten) {
        int h = daten.length;
        int w = daten[0].length;

        buttons = new JButton[h][w];
        spielfeld.removeAll();
        spielfeld.setLayout(new GridLayout(h, w));

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                JButton feld = new JButton();
                feld.setFont(new Font("Arial", Font.PLAIN, 40));
                feld.setFocusPainted(false);
                feld.setPreferredSize(new Dimension(80, 80));
                feld.setText(daten[i][j].equals("empty") ? "" : daten[i][j]);

                final int row = i;
                final int col = j;
                feld.addActionListener(e -> {
                    try {
                        onFieldClicked(row, col, feld);
                    } catch (SocketException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                buttons[i][j] = feld;
                spielfeld.add(feld);
            }
        }

        spielfeld.revalidate();
        spielfeld.repaint();
    }

    private void updateWinCondition() {
        try {
            winCondition = Integer.parseInt(value.getText());
            if (winCondition < 2) {
                JOptionPane.showMessageDialog(frame, "Die Gewinnbedingung muss mindestens 2 sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                winCondition = 3;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Bitte eine gültige Zahl für die Gewinnbedingung eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
            winCondition = 3;
        }
    }

    private void onFieldClicked(int row, int col, JButton feld) throws SocketException {
        System.out.println("Feld geklickt: Zeile " + row + ", Spalte " + col);

        if (!feld.getText().isEmpty()) {
            return; // Bereits belegt – nichts tun
        }

        TicTacToeField.set_cross(Spiellogik.getPlayer().getUsername(), row, col, true);
        feld.setText(Spiellogik.getPlayer().getUsername());
    }

    private void onJoinClicked() throws SocketException {
        Json_converter.create_JSON(Json_converter.Message_type.JOIN, 0, 0);
        // Feld wird durch INIT-Nachricht gesetzt
    }

    public void set_gui_cross(String username, int row, int col) {

        buttons[row][col].setText(username);

    }
    public int getWinCondition() {
        return winCondition;
    }

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                UDP_communication.receive_udp();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Gib deinen Usernamen an: ");
        String username = scanner.nextLine();

        System.out.println("Gib deinen Port an: ");
        int port = scanner.nextInt();

        Spiellogik.setPlayer(new Player(username, port));
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
