package HTTPHandlers;
import GameHandlers.Player;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class OHHellHttpServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        //defines all route paths into their methods.
        server.createContext("/Connect", new ConnectHandler());
        server.createContext("/Disconnect", new DisconnectHandler());
        server.createContext("/ListPlayers", new ListPlayers());
        server.createContext("/PlayCard", new PlayCardHandler());
        server.setExecutor(null); // Creates a default executor
        server.start();
        System.out.println("HTTP server started on port 8080");
    }
    //Handles initial connections
    //We want to add this player to the online list
    static class ConnectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Connections request received");

            String response = JsonHandler.getConnectionResultsFromExchange(exchange);

            System.out.println("Result sent:"+ response);

        }
    }
    //handles the disconnect call. We return nothing here
    static class DisconnectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Disconnect request received");
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            //If we failed to get a player properly, print so.
            try{

                JSONObject jsonObject = new JSONObject(requestBody);
                String connectionID = jsonObject.getString("connectionID");
                Player offlinePlayer = Player.removeOnlinePlayer( connectionID);
                String response = "{\"message\":\"Goodbye\"}";
                System.out.println("GameHandler.Player Name: " + offlinePlayer.getUsername() + " is now disconnected.");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch(Exception e){
                //If our jwk does process into a player and sub,
                System.out.println("Bad Disconnect Request.");
                String response = "{\"error\":\"Bad Disconnect Request. If someone was supposed to be offline back here, they are not\"}";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }


    //Handles all /PlayCard calls.
    static class PlayCardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Card Move Received";
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            System.out.println("Received move: " + requestBody);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    //Returns a list of players connected to the websocket server
    static class ListPlayers implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Players are:";
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            System.out.println("GameHandler.Player list requested" + requestBody);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
