import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.*;
import java.util.List;

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

    private JButton[][] buttons;

    // Ranking
    private Map<String, Integer> ranking = new HashMap<>();
    private DefaultListModel<String> rankingModel;
    private JList<String> rankingList;

    public TicTacToeGUI() {
        instance = this;
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
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

        // Ranking-Anzeige
        rankingModel = new DefaultListModel<>();
        rankingList = new JList<>(rankingModel);
        rankingList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rankingList.setBorder(BorderFactory.createTitledBorder("Ranking"));
        JScrollPane rankingScroll = new JScrollPane(rankingList);
        rankingScroll.setPreferredSize(new Dimension(150, 0));

        // Spielfeld
        spielfeld = new JPanel();
        spielfeld.setLayout(new GridLayout(3, 3));

        neuesSpielButton.addActionListener(e -> {
            updateWinCondition();
            erstelleSpielfeld();
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
        frame.add(rankingScroll, BorderLayout.EAST);
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
                if (daten[i][j].equals("empty")) {
                    feld.setText("");
                }
                else {
                    feld.setBackground(getColorForString(daten[i][j]));
                }
                //feld.setText(daten[i][j].equals("empty") ? "" : daten[i][j]);

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
        if (!feld.getText().isEmpty()) {
            return; // Feld bereits belegt
        }

        TicTacToeField.set_cross(Spiellogik.getPlayer().getUsername(), row, col, true);
        //feld.setText(Spiellogik.getPlayer().getUsername());
        buttons[row][col].setBackground(getColorForString(Spiellogik.getPlayer().getUsername()));
        Spiellogik.check_for_point(row, col);
    }

    private void onJoinClicked() throws SocketException {
        Json_converter.create_JSON(Json_converter.Message_type.JOIN, 0, 0);
    }

    public void set_gui_cross(String username, int row, int col) {
        buttons[row][col].setBackground(getColorForString(username));
        //buttons[row][col].setText(username);
        Spiellogik.check_for_point(row, col);
    }

    public void updateRanking(Map<String, Integer> ranking) {
        rankingModel.clear();

        ranking.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // absteigend nach Punkten
                .forEach(entry -> rankingModel.addElement(entry.getKey() + ": " + entry.getValue()));
    }


    public void resetField() {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setText("");
            }
        }
    }

    public int getWinCondition() {
        return winCondition;
    }

    private Color getColorForString(String value) {
        // Nutzerabhängige Farbe
        int hash = value.hashCode();
        //rot, grün und blau wert berechnen
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = (hash & 0x0000FF);
        return new Color(r, g, b).brighter();
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

        Map<String, Integer> ranking = new HashMap<>();
        ranking.put(username, 0);
        Spiellogik.setPunktestand(ranking);
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
