package HTTPHandlers;

import GameHandlers.Player;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;

public class Disconnect {


    //Expected String Input:
    //{"action":"something","jwk":"something", "connectionID":"something"}
    public static void disconnectPlayer(HttpExchange exchange)throws IOException {
        try{
            JSONObject infoJson = ExchangeHandler.getInfoJsonFromExchange(exchange);
            //Lets ID player by incoming connectionID
            Player.removeOnlinePlayer(infoJson.getString("connectionID"));
        }catch (Exception e){
            System.out.println("Error in disconnect: "+e);
        }
    }
}
