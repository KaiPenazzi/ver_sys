import org.json.*;

import java.util.HashMap;

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
            case "Action":
                message.setType("Action");
                ActionMessageData actiondata = new ActionMessageData(obj.getInt("x"), obj.getInt("y"), obj.getString("user") );
                message.setData(actiondata);
                break;
            case "Init":
                message.setType("Init");

                JSONObject pointObj = obj.getJSONObject("points");
                HashMap<String,Integer> points = new HashMap<String,Integer>();
                //Rangliste parsen
                for ( String key : pointObj.keySet()){
                    int val = pointObj.getInt(key);
                    points.put(key, val);
                }

                JSONArray boardArr = obj.getJSONArray("board");
                //board parsen
                int size = boardArr.length();
                String[][] fields = new String[size][size];
                for (int i = 0; i < size; i++){
                    JSONArray row = boardArr.getJSONArray(i);
                    for(int j = 0; j < size; j++){
                        String username = row.getString(j);
                        fields[i][j] = username;
                    }
                }
                Board board = new Board(size, size);
                board.setBoard(fields);
                InitMessageData initData = new InitMessageData(points, board);
                message.setData(initData);

                break;
            case "Join":
                message.setType("Join");
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
            case "Action":
                ActionMessageData actionData = (ActionMessageData) message.getData();
                obj.put("type", "Action");
                obj.put("x", actionData.getX());
                obj.put("y", actionData.getY());
                obj.put("user",actionData.getUsr());
                jsonString = obj.toString(4);
                System.out.println(jsonString);
                break;
            case "Init":
                InitMessageData initData = (InitMessageData) message.getData();
                obj.put("type", "Init");
                JSONArray boardJson = new JSONArray();
                for (String[] row : initData.getBoard().getBoard() ){
                    JSONArray jRow = new JSONArray();
                    for(String field : row){
                        jRow.put(field);
                    }
                    boardJson.put(jRow);
                }
                obj.put("board", boardJson);
                JSONObject pointsJson = new JSONObject();
                for(String user : initData.points.keySet()){
                    pointsJson.put(user, initData.points.get(user));
                }
                obj.put("points", pointsJson);
                jsonString = obj.toString(4);

                break;

            case "Join":
                obj.put("type","Join");
                jsonString = obj.toString(4);
                break;
        }

        return jsonString;
    }


}


