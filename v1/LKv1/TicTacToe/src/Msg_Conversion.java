import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Klasse um aus Json Files, die als String ankommen in eine Message zu parsen
 * und Messages als Json Files zu Serialisieren
 */
public class Msg_Conversion {

    /**
     * msg wird in Message Objekt geparst. vorher Wird der typ der Nachricht ausgelesen
     * @param msg die per UDP empfangene Message
     * @return Message Objekt
     */
    public static Message processIncomingMessage(String msg){
        System.out.println("test" + msg);
        JSONObject obj = new JSONObject(msg);
        Message message = new Message();
        String type = obj.getString("type");
        switch (type){
            case "action":
                message.setType("action");
                ActionMessageData actiondata = new ActionMessageData(obj.getInt("x"), obj.getInt("y"), obj.getString("usr") );
                message.setData(actiondata);
                break;
            case "init":
                message.setType("init");

                JSONObject pointObj = obj.getJSONObject("score");
                HashMap<String,Integer> points = new HashMap<String,Integer>();

                InitMessageData initData = new InitMessageData(null,null);
                //Rangliste parsen
                for ( String key : pointObj.keySet()){
                    int val = pointObj.getInt(key);
                    points.put(key, val);
                }
                if(obj.has("field")) {
                    JSONArray boardArr = obj.getJSONArray("field");
                    //board parsen
                    int size = boardArr.length();
                    String[][] fields = new String[size][size];
                    for (int i = 0; i < size; i++) {
                        JSONArray row = boardArr.getJSONArray(i);
                        for (int j = 0; j < size; j++) {
                            String username = row.getString(j);
                            fields[i][j] = username;
                        }
                    }

                    Board board = new Board(size, size);
                    board.setBoard(fields);
                      initData = new InitMessageData(points, board);
                }else {
                     initData.setPoints(points);
                }
                message.setData(initData);
                break;
            case "join":
                message.setType("join");
                JoinMessageData joinData = new JoinMessageData(obj.getString("usr"), obj.getInt("port"),obj.getString("ip") );
                message.setData(joinData);
                break;
            case "player":
                message.setType("player");


                List<Player> playerList = new ArrayList<>();
                JSONArray playerListJson = obj.getJSONArray("players");
                for (int i = 0; i < playerListJson.length(); i++) {
                    JSONObject jsonPlayer = playerListJson.getJSONObject(i);
                    String usr = jsonPlayer.optString("usr");
                    int port = jsonPlayer.getInt("port");
                    String ip = jsonPlayer.getString("ip");
                    Player newP = new Player(usr, port, ip);
                    playerList.add(newP);
                }

                PlayerMessageData playerData = new PlayerMessageData(playerList);
                message.setData(playerData);

                break;
            case "leave":
                message.setType("leave");

                LeaveMessageData leaveMessageData = new LeaveMessageData(obj.getString("usr"), obj.getInt("port"),obj.getString("ip") );
                message.setData(leaveMessageData);
                break;
            case "new_player":
                message.setType("new_player");
                NewPlayerMessageData newPlayerData = new NewPlayerMessageData(obj.getString("usr"), obj.getInt("port"),obj.getString("ip"));
                message.setData(newPlayerData);
                break;


        }
        return message;
    }

    /**
     * serialisiert übergebene Message Datei in einen JSON String
     * @param message zu versendende Nachricht
     * @return Json File als String
     */
    public static String createJSONFromMessage(Message message){

        JSONObject obj = new JSONObject();
        String jsonString = "";
        switch(message.getType()){
            case "action":
                ActionMessageData actionData = (ActionMessageData) message.getData();
                obj.put("type", "action");
                obj.put("x", actionData.getX());
                obj.put("y", actionData.getY());
                obj.put("usr",actionData.getUsr());
                jsonString = obj.toString(4);
                System.out.println(jsonString);
                break;
            case "init":
                InitMessageData initData = (InitMessageData) message.getData();
                obj.put("type", "init");
                JSONArray boardJson = new JSONArray();
                for (String[] row : initData.getBoard().getBoard() ){
                    JSONArray jRow = new JSONArray();
                    for(String field : row){
                        jRow.put(field);
                    }
                    boardJson.put(jRow);
                }
                obj.put("field", boardJson);
                obj.put("k",initData.getBoard().getK());
                JSONObject pointsJson = new JSONObject();
                for(String user : initData.points.keySet()){
                    pointsJson.put(user, initData.points.get(user));
                }
                obj.put("score", pointsJson);
                jsonString = obj.toString(4);
                break;

            case "join":
                JoinMessageData joinData = (JoinMessageData) message.getData();

                obj.put("type","join");
                obj.put("usr",joinData.getUsr() );
                obj.put("port", joinData.getPort());
                obj.put("ip",joinData.getIp());
                jsonString = obj.toString(4);
                break;

            case "player":
                PlayerMessageData playerData = (PlayerMessageData) message.getData();

                obj.put("type", "player");
                JSONArray playerList = new JSONArray();
                for(Player p : playerData.getPlayers()){
                    JSONObject playerObj = new JSONObject();
                    playerObj.put("usr", p.getUsr());
                    playerObj.put("port",p.getPort());
                    playerObj.put("ip", p.getIp());
                    playerList.put(playerObj);
                }
                obj.put("players", playerList);
                jsonString = obj.toString(4);
                break;

            case "leave":
                LeaveMessageData leaveData = (LeaveMessageData) message.getData();
                obj.put("type", "leave");
                obj.put("usr",leaveData.getUsr() );
                obj.put("port", leaveData.getPort());
                obj.put("ip",leaveData.getIp());
                jsonString = obj.toString(4);
                break;

            case "new_player":
                NewPlayerMessageData newPData = (NewPlayerMessageData) message.getData();
                obj.put("type", "new_player");
                obj.put("usr",newPData.getUsr() );
                obj.put("port", newPData.getPort());
                obj.put("ip",newPData.getIp());
                jsonString = obj.toString(4);
                break;
        }


        return jsonString;
    }


}


