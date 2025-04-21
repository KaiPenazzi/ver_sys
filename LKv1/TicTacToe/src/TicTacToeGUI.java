import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.HashMap;

public class TicTacToeGUI {

    private JFrame frame;
    private JTextField usernameField, portField, fieldSizeField, kField;
    private JButton joinButton, initButton;
    private JPanel boardPanel;
    private int port;
    private String username;
    private int fieldSize, k;
    private String[][] board;
    private JList<String> rankingList;
    private Game game;
    private DefaultListModel<String> rankingModel = new DefaultListModel<>();


    //TODO: MessageData Klassen ersellen für newPlayer, Join und Playerlist
    public TicTacToeGUI() {
        createGUI();
    }

    private void createGUI() {

        frame = new JFrame("Tic Tac Toe Multiplayer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        usernameField = new JTextField("Username");
        portField = new JTextField("Port");
        joinButton = new JButton("Join");

        fieldSizeField = new JTextField("Field Size");
        kField = new JTextField("K");
        initButton = new JButton("Init");

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Port:"));
        inputPanel.add(portField);
        inputPanel.add(joinButton);

        inputPanel.add(new JLabel("Field Size:"));
        inputPanel.add(fieldSizeField);
        inputPanel.add(new JLabel("K:"));
        inputPanel.add(kField);
        inputPanel.add(initButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        boardPanel = new JPanel();

        rankingList = new JList<>(rankingModel);
        JScrollPane rankingScrollPane = new JScrollPane(rankingList);
        rankingScrollPane.setPreferredSize(new Dimension(150, 0));
        rankingScrollPane.setBorder(BorderFactory.createTitledBorder("Ranking"));

        // Container für Mitte (Spielfeld) und rechts (Ranking)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(boardPanel, BorderLayout.CENTER);
        centerPanel.add(rankingScrollPane, BorderLayout.EAST);

        frame.add(centerPanel, BorderLayout.CENTER);


        // Button Listener
        joinButton.addActionListener(e -> {
            username = usernameField.getText();
            port = Integer.parseInt(portField.getText());
            usernameField.setEditable(false);
            portField.setEditable(false);
            joinButton.setEnabled(false);
            try {
                UDP_Com.socketInitializer(port);
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
            game = new Game(new Board(), new HashMap<String, Integer>(), port);
            Message joinMessage = new Message("join", null);
            UDP_Com.send_UDP(joinMessage);
            // warte 2 sek ob eine Init nachricht kommt
            Message returnedMessage = UDP_Com.recv_UDP();
            if (returnedMessage != null) {
                if (returnedMessage.getType().equals("join")) {
                    //es existiert noch kein spiel/ keine Init erhalten

                } else if (returnedMessage.getType().equals("init")) {
                    InitMessageData msg = (InitMessageData) returnedMessage.getData();
                    //System.out.println(msg.getBoard().getFieldSize() + msg.getBoard().getK());
                    if(msg.getBoard() == null){
                        game.setRanking(msg.points);
                    }else{
                        game.setGameBoard(msg.getBoard());
                        fieldSize = game.getGameBoard().getFieldSize();
                        k = game.getGameBoard().getK();
                        game.setRanking(msg.points);
                        game.setRunning(true);
                        fieldSizeField.setEditable(false);
                        initButton.setEnabled(false);
                        kField.setEnabled(false);
                        initBoard(game.getGameBoard());
                    }

                }
            }
            //fange an auf Nachrichten zu warten
            new Thread(this::messageListener).start();
        });

        initButton.addActionListener(e -> {
            try {
                if (!game.isRunning()) {
                    Board b = new Board(Integer.parseInt(fieldSizeField.getText()), Integer.parseInt(kField.getText()));
                    game.setGameBoard(b);
                    // System.out.println(game.getGameBoard().getFieldSize());
                    initBoard(game.getGameBoard());
                    game.setRunning(true);
                    InitMessageData initData = new InitMessageData(game.getRanking(), game.getGameBoard());
                    Message initMsg = new Message("init", initData);
                    UDP_Com.send_UDP(initMsg);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Bitte gültige Zahlen eingeben.");
            }
        });

        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private void initBoard(Board b) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(b.getFieldSize(), b.getFieldSize()));
        for (int y = 0; y < b.getFieldSize(); y++) {
            for (int x = 0; x < b.getFieldSize(); x++) {
                JButton cellButton = new JButton();
                int finalX = x;
                int finalY = y;
                //game.getGameBoard().printBoardTest();

                if (game.getGameBoard().getBoard()[x][y].equals("none")) {
                    cellButton.setBackground(Color.LIGHT_GRAY);
                } else
                    cellButton.setBackground(getColorForString(game.getGameBoard().getBoard()[x][y]));
                cellButton.addActionListener(e -> {
                    if (game.move(finalX, finalY, username)) {
                        //sende move per udp
                        ActionMessageData moveData = new ActionMessageData(finalX, finalY, username);
                        Message moveMessage = new Message("action", moveData);
                        UDP_Com.send_UDP(moveMessage);
                        updateBoardButtonColors();
                        //game.getGameBoard().printBoardTest();
                        refreshRankingDisplay();
                        boardPanel.revalidate();
                        boardPanel.repaint();

                    }
                    ;
                });

                boardPanel.add(cellButton);
            }

            boardPanel.revalidate();
            boardPanel.repaint();
        }
        updateBoardButtonColors();
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
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }

    private void refreshRankingDisplay() {
        rankingModel.clear();
        for (String user : game.getRanking().keySet()) {
            rankingModel.addElement(user + ": " + game.getRanking().get(user));
        }
    }


    private void updateBoardButtonColors() {
        for (int y = 0; y < game.getGameBoard().getFieldSize(); y++) {
            for (int x = 0; x < game.getGameBoard().getFieldSize(); x++) {
                Component c = boardPanel.getComponent(y * game.getGameBoard().getFieldSize() + x);
                if (c instanceof JButton button) {
                    String val = game.getGameBoard().getBoard()[y][x];

                    // Farbe setzen
                    if (val.equalsIgnoreCase("none")) {
                        button.setText("");
                        button.setBackground(Color.LIGHT_GRAY);
                    } else {
                        button.setText(val);
                        button.setBackground(getColorForString(val));
                    }

                    // Sicherstellen, dass Farben angezeigt werden
                    button.setOpaque(true);
                    button.setContentAreaFilled(true);
                }
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    /**
     *Threadfunktion um dauerhaft messages zu empfangen und zu verarbeiten
     *Init nachrichten werden nur berücksichtigt wenn das spiel noch nicht gestartet hat
     * bei Empfangener Join wird eine Init gesendet
     * Move löst einfach die move funktion aus
     */
    private void messageListener() {
        while (true) {
            Message incomingMessage = UDP_Com.recv_UDP();

            if (incomingMessage != null) {
                System.out.println(incomingMessage.getType());
                switch (incomingMessage.getType()) {
                    case "init":
                        if (!game.isRunning()) {
                            InitMessageData initMessage = (InitMessageData) incomingMessage.getData();
                            if(initMessage.getBoard() != null){
                                game.setGameBoard(initMessage.getBoard());
                                initBoard(game.getGameBoard());
                                updateBoardButtonColors();
                                refreshRankingDisplay();
                                initButton.setEnabled(false);
                                game.setRunning(true);
                            }
                            game.setRanking(initMessage.points);
                        }
                        break;
                    case "action":
                        if (game.isRunning()) {
                            ActionMessageData actionMessage = (ActionMessageData) incomingMessage.getData();
                            game.move(actionMessage.getX(), actionMessage.getY(), actionMessage.getUsr());
                            updateBoardButtonColors();
                            refreshRankingDisplay();
                        }
                        break;

                    case "join":
                        if (game.isRunning()) {
                            JoinMessageData joinMessage = (JoinMessageData) incomingMessage.getData();
                            Player  newPlayer = new Player(joinMessage.getUsr(), joinMessage.getPort(), joinMessage.getIp());
                            InitMessageData joinAnswerdata = new InitMessageData(game.getRanking(), game.getGameBoard());
                            Message joinAnswer = new Message("init", joinAnswerdata);
                            UDP_Com.send_UDP(joinAnswer,newPlayer);

                            NewPlayerMessageData newPlayerMsgData = new NewPlayerMessageData(joinMessage.getUsr(), joinMessage.getPort(), joinMessage.getIp());
                            Message newPlMsg = new Message("new_player", newPlayerMsgData);
                            UDP_Com.send_UDP(newPlMsg);

                            Game.players.add(newPlayer);

                            //player list an neuen Spieler senden
                            PlayerMessageData playerListMsgData = new PlayerMessageData(Game.players);
                            Message playerListMsg = new Message("player", playerListMsgData);
                            UDP_Com.send_UDP(playerListMsg, newPlayer);
                        }

                        break;

                    case "player":
                        PlayerMessageData playerMessage = (PlayerMessageData) incomingMessage.getData();
                        Game.players = playerMessage.getPlayers();

                        refreshRankingDisplay();
                        break;
                    case "leave":
                        LeaveMessageData leaveMsgData = (LeaveMessageData)  incomingMessage.getData();
                        Player deletedPlayer = new Player(leaveMsgData.getUsr(), leaveMsgData.getPort(), leaveMsgData.getIp());
                        Game.players.remove(deletedPlayer);
                        break;
                    case "new_player":
                        NewPlayerMessageData newPlayerMsgData = (NewPlayerMessageData) incomingMessage.getData();
                        Player newPlayer = new Player(newPlayerMsgData.getUsr(), newPlayerMsgData.getPort(), newPlayerMsgData.getIp());
                        Game.players.add(newPlayer);

                        break;
                }
            }
            refreshRankingDisplay();
            boardPanel.revalidate();
            boardPanel.repaint();
        }
    }


}

