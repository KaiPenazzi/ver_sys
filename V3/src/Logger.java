
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Logger {


    private String address;
    private int infoCounter ;
    private int echoCounter ;

    public Logger(String address) {
        this.address = address;
        this.infoCounter = 0;
        this.echoCounter = 0;
    }

    public Message recv(){

        Message msg = null;
            try {
                msg = UDPCon.recvMessage();
                switch(msg){
                    case LogMessage logMessage:
                        if(logMessage.getMsg_type().equals("i"))
                        {
                            infoCounter++;
                        } else if (logMessage.getMsg_type().equals("e")) {
                            echoCounter++;
                        }

                        msg = logMessage;
                        break;
                    case ResultMessage resultMessage:
                        msg = resultMessage;
                        break;
                    default:
                        break;
                }

                return msg;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }



        }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getInfoCounter() {
        return infoCounter;
    }

    public void setInfoCounter(int infoCounter) {
        this.infoCounter = infoCounter;
    }

    public int getEchoCounter() {
        return echoCounter;
    }

    public void setEchoCounter(int echoCounter) {
        this.echoCounter = echoCounter;
    }
}
