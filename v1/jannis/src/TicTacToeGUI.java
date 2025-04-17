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
    private JTextField friendIPField;     // NEU
    private JTextField friendPortField;   // NEU
    private JFrame frame;
    private int winCondition = 3;

    private JButton[][] buttons;

    private Map<String, Integer> ranking = new HashMap<>();
    private DefaultListModel<String> rankingModel;
    private JList<String> rankingList;

    private DefaultListModel<String> onlineModel;
    private JList<String> onlineList;

    public TicTacToeGUI() {
        instance = this;
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Fenstergröße wurde auf 1200x800 vergrößert.
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        width = new JTextField("3", 3);
        height = new JTextField("3", 3);
        value = new JTextField("3", 3);

        friendIPField = new JTextField("127.0.0.1", 10);    // NEU
        friendPortField = new JTextField("12345", 5);       // NEU

        neuesSpielButton = new JButton("Neues Spiel");
        joinButton = new JButton("Join");

        controlPanel.add(new JLabel("Breite:"));
        controlPanel.add(width);
        controlPanel.add(new JLabel("Höhe:"));
        controlPanel.add(height);
        controlPanel.add(new JLabel("Punkte für Sieg:"));
        controlPanel.add(value);

        controlPanel.add(new JLabel("IP Freund:"));        // NEU
        controlPanel.add(friendIPField);                   // NEU
        controlPanel.add(new JLabel("Port Freund:"));       // NEU
        controlPanel.add(friendPortField);                 // NEU

        controlPanel.add(neuesSpielButton);
        controlPanel.add(joinButton);

        rankingModel = new DefaultListModel<>();
        rankingList = new JList<>(rankingModel);
        rankingList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rankingList.setBorder(BorderFactory.createTitledBorder("Ranking"));
        JScrollPane rankingScroll = new JScrollPane(rankingList);
        rankingScroll.setPreferredSize(new Dimension(150, 0));

        onlineModel = new DefaultListModel<>();
        onlineList = new JList<>(onlineModel);
        onlineList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        onlineList.setBorder(BorderFactory.createTitledBorder("Online Spieler"));
        JScrollPane onlineScroll = new JScrollPane(onlineList);
        onlineScroll.setPreferredSize(new Dimension(150, 0));

        JPanel sidePanel = new JPanel(new GridLayout(2, 1));
        sidePanel.add(rankingScroll);
        sidePanel.add(onlineScroll);

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
                onJoinClicked();  // updated
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
        });

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(spielfeld, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);
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
                    Color bg = new Color(238, 238, 238).brighter();
                    feld.setBackground(bg);

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
                Color bg = new Color(238, 238, 238).brighter();
                feld.setBackground(bg);
                if (daten[i][j].equals("empty")) {
                    feld.setText("");
                } else {
                    feld.setBackground(getColorForString(daten[i][j]));
                }

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
            return;
        }

        TicTacToeField.set_cross(Spiellogik.getPlayer().getUsername(), row, col, true);
        buttons[row][col].setBackground(getColorForString(Spiellogik.getPlayer().getUsername()));
        Spiellogik.check_for_point(row, col);
    }

    private void onJoinClicked() throws SocketException {
        String ip = friendIPField.getText().trim();
        String portText = friendPortField.getText().trim();

        int port;
        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Ungültiger Port für Freund!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Versuche Beitritt zu: " + ip + ":" + port);

        // Anpassung je nach deiner Methode – hier als Platzhalter:
        Json_converter.create_JSON(Json_converter.Message_type.JOIN, 4, 4, ip, port);
    }

    public void set_gui_cross(String username, int row, int col) {
        buttons[row][col].setBackground(getColorForString(username));
        Spiellogik.check_for_point(row, col);
    }

    public void updateRanking(Map<String, Integer> ranking) {
        rankingModel.clear();
        ranking.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> rankingModel.addElement(entry.getKey() + ": " + entry.getValue()));
    }

    public void updateOnlinePlayers(List<Player> playersOnline) {
        List<String> playersOnline_names = Spiellogik.getPlayers_name();

        onlineModel.clear();
        playersOnline_names.stream().sorted().forEach(onlineModel::addElement);
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
        if(value.equals("none"))
        {
            return new Color(238, 238, 238).brighter();  // Ein helles Grau

        }

        int hash = value.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = (hash & 0x0000FF);
        return new Color(r, g, b).brighter();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Gib deinen Usernamen an: ");
        String username = scanner.nextLine();

        System.out.println("Gib deine Ip Adresse an: ");
        String ip = scanner.nextLine();

        System.out.println("Gib deinen Port an: ");
        int port = scanner.nextInt();

        Player my_Player = new Player(username, ip, port);
        Spiellogik.setPlayer(my_Player);
        Spiellogik.addPlayerToList(my_Player);

        new Thread(() -> {
            try {
                UDP_communication.receive_udp();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Map<String, Integer> ranking = new HashMap<>();
        ranking.put(username, 0);
        Spiellogik.setPunktestand(ranking);

        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
