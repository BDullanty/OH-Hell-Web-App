import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class OHHellHttpServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        //defines all route paths into their methods.
        server.createContext("/Connect", new ConnectHandler());
        server.createContext("/PlayCard", new PlayCardHandler());
        server.createContext("/ListPlayers", new ListPlayers());
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
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            System.out.println("RequestBody:"+requestBody);
            Player connectingPlayer = JsonHandler.getPlayerFromBody(requestBody);
            Player.addPlayerOnline(connectingPlayer);
            System.out.println("Player Name: " + connectingPlayer.getUsername()+", with Sub ID of:"+connectingPlayer.getSub()+" Is now online.");
            //And return our response
            String response = "{\"Player\": "+connectingPlayer.getUsername()+", \"Sub\": "+connectingPlayer.getSub()+"}";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
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
            System.out.println("Player list requested" + requestBody);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
