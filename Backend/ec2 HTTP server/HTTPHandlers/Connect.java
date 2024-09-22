package HTTPHandlers;

import GameHandlers.User;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import GameHandlers.Player;

import java.io.IOException;
import java.io.OutputStream;

import static HTTPHandlers.ExchangeHandler.getInfoJsonFromExchange;

public class Connect {


    //Expected String Input:
    //{"action":"something","jwk":"something", "connectionID":"something"}
    public static String connectPlayer(HttpExchange exchange) throws IOException {
        String response;
        try {
            //parse exhange into json with info.
            JSONObject infoJson = getInfoJsonFromExchange(exchange);
            User connectingPlayer = getUserFromBody(infoJson);
            //If  our jwk does process into a player and sub,
            User.addUserOnline(connectingPlayer);
            System.out.println("Player " + connectingPlayer.getUsername() + " is now connected.");
            //And return our response
            response = "{\"Player\": \"" + connectingPlayer.getUsername() + "\"}";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            //Lets notify everyone that someone connected:
            PostAllUsersToLobby.postAllUsersToLobby();
            PostAllGamesToLobby.postAllGamesToLobby();

        } catch (Exception e) {
            //If we failed to get a player properly, return 400
            System.out.println("Bad Connect Request.");
            response = "{\"error\":\"Bad Connect Request\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        return response;
    }

    public static User getUserFromBody(JSONObject infoJson) {
        try {
            return User.getUser(infoJson.getString("sub"), infoJson.getString("username"), infoJson.getString("connectionID"));
        } catch (Exception e) {
            System.out.println("Error when creating player.." + e);
            return null;
        }


    }
}