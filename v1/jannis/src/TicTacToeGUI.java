import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class TicTacToeGUI {
    private JButton neuesSpielButton;
    private JButton joinButton;
    private JPanel spielfeld;
    private JTextField width;
    private JTextField height;
    private JTextField value; // Textfeld für die Gewinnbedingung
    private JFrame frame;
    private int winCondition = 3; // Standardwert für Gewinnbedingung

    public TicTacToeGUI() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        // Steuer-Panel mit Eingabefeldern und Buttons
        JPanel controlPanel = new JPanel();
        width = new JTextField("3", 3);
        height = new JTextField("3", 3);
        value = new JTextField("3", 3); // Standardwert für Gewinnbedingung

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

        // Spielfeld-Panel
        spielfeld = new JPanel();
        spielfeld.setLayout(new GridLayout(3, 3)); // Standardgröße

        // ActionListener für den "Neues Spiel"-Button
        neuesSpielButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWinCondition(); // Gewinnbedingung updaten
                erstelleSpielfeld();
                Spiellogik.start_new_Game(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
            }
        });

        // ActionListener für den "Join"-Button
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onJoinClicked();
            }
        });

        // Komponenten zum Frame hinzufügen
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(spielfeld, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void erstelleSpielfeld() {
        try {
            int w = Integer.parseInt(width.getText());
            int h = Integer.parseInt(height.getText());

            spielfeld.removeAll();  // Altes Spielfeld leeren
            spielfeld.setLayout(new GridLayout(h, w)); // Neues Layout setzen

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    JButton feld = new JButton("");
                    feld.setFont(new Font("Arial", Font.PLAIN, 40));
                    feld.setFocusPainted(false);
                    feld.setPreferredSize(new Dimension(80, 80));

                    // MouseListener hinzufügen
                    final int row = i;
                    final int col = j;
                    feld.addActionListener(e -> onFieldClicked(row, col, feld));

                    spielfeld.add(feld);
                }
            }

            spielfeld.revalidate(); // Layout aktualisieren
            spielfeld.repaint();    // Neu zeichnen

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Bitte gültige Zahlen für Breite und Höhe eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateWinCondition() {
        try {
            winCondition = Integer.parseInt(value.getText());
            if (winCondition < 2) {
                JOptionPane.showMessageDialog(frame, "Die Gewinnbedingung muss mindestens 2 sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                winCondition = 3; // Standardwert setzen
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Bitte eine gültige Zahl für die Gewinnbedingung eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
            winCondition = 3; // Standardwert setzen
        }
    }

    private void onFieldClicked(int row, int col, JButton feld) {
        System.out.println("Feld geklickt: Zeile " + row + ", Spalte " + col);

        TicTacToeField.set_cross(row, col, true);
        feld.setText(Spiellogik.getPlayer().getUsername());
    }

    private void onJoinClicked() {
        JOptionPane.showMessageDialog(frame, "Join-Funktion noch nicht implementiert!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public int getWinCondition() {
        return winCondition;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Gib deinen Usernamen an: ");
        String username = scanner.nextLine();

        System.out.println("Gib deinen Port an: ");
        int port = scanner.nextInt();

        Spiellogik.setPlayer(new Player(username, port));
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
