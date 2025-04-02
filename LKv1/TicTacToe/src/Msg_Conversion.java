import org.json.*;
public class Msg_Conversion {

    public static Message processIncomingMessage(String message){

        return new Message();
    }
//    public static Message processInitMessage(String message){
//
//    }

    public static String createJSONFromMessage(Message message){

        switch(message.getType()){
            case "Action":
                
                break;
            case "Init":

                break;

            case "Join":
                break;
        }

        return "";
    }



//    public static String createJSONFromActionMessage(Message message){
//
//        return "";
//    }
//    public static String createJSONFromInitMessage(Message message){
//        return "";
//    }
}


