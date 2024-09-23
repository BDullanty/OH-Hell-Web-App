package HTTPHandlers;
import GameHandlers.Game;
import GameHandlers.GameHandler;
import GameHandlers.State;
import GameHandlers.User;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static HTTPHandlers.ExchangeHandler.getInfoJsonFromExchange;

public class HTTPServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        //defines all route paths into their methods.
        server.createContext("/Connect", new ConnectHandler());
        server.createContext("/Disconnect", new DisconnectHandler());
        server.createContext("/ListPlayers", new ListPlayers());
        server.createContext("/CreateGame", new CreateGameHandler());
        server.createContext("/LeaveGame", new LeaveGameHandler());
        server.createContext("/VoteStart", new StartGameHandler());
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
            String response = Connect.connectPlayer(exchange);
            System.out.println("Result sent:"+ response);
        }
    }

    static class DisconnectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Disconnect.disconnectPlayer(exchange);
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
    static class CreateGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Received game creation request"  );
            String response = "";
            try{
                String body = new String(exchange.getRequestBody().readAllBytes());
                System.out.println("Game creation body: "+body  );
                JSONObject infoJson= new JSONObject(body);

                User u = User.getUser(infoJson.getString("connectionID"));
                System.out.println("got user " + u.getUsername() +" and they have a gameID of "+u.getGameID());
                if(u.getGameID() != -1) {
                    Game oldGame = GameHandler.getGame(u.getGameID());
                    GameHandler.removeUserFromGame(u);
                    if(oldGame.getPlayers().isEmpty()) GameHandler.end(oldGame);
                }
                Game game = new Game(u);
                GameHandler.addGameToLobby(game);
                System.out.println("Created game with ID "+game.getGameID()+" with host "+u.getUsername());
                response = "{\"gameID\" : \""+game.getGameID()+"\"}";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                PostAllGamesInfo.postAllGamesToLobby();
            } catch(Exception e){
                System.out.println("Error when creating game: "+e);
                response = "{\"error\":\"Bad create game request:\n"+e+"\"}";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        }
    }
    static class LeaveGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Received game leave request"  );
            String response = "";
            try{
                String body = new String(exchange.getRequestBody().readAllBytes());
                System.out.println("Game leave body: "+body  );
                JSONObject infoJson = new JSONObject(body);
                User u = User.getUser(infoJson.getString("connectionID"));
                System.out.println("got user " + u.getUsername() );
                GameHandler.removeUserFromGame(u);
                PostAllGamesInfo.postAllGamesToLobby();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                PostAllGamesInfo.postAllGamesToLobby();
            } catch(Exception e){
                System.out.println("Error when creating game: "+e);
                response = "{\"error\":\"Bad create game request:\n"+e+"\"}";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        }
    }
    static class StartGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Received voteStart request");

            try{
                String response = "";
                String body = new String(exchange.getRequestBody().readAllBytes());
                System.out.println("Game creation body: "+body  );
                JSONObject infoJson= new JSONObject(body);

                User requestingUser = User.getUser(infoJson.getString("connectionID"));
                System.out.println("Requested by user " + requestingUser.getUsername() );
                //Requires json to have gameID with gameID
                Game game = GameHandler.getGame(requestingUser.getGameID());
                if( game.getState() != State.WAITING){
                    throw new IllegalAccessException("Requesting user "+requestingUser.getUsername()+" is trying to start a game that is not in waiting ");
                }
                if(requestingUser.hasVoted()){
                    throw new IllegalAccessError("Already voted");
                }

                requestingUser.setVoted();

                if(GameHandler.everyoneVotedStart(game)) GameHandler.start(game);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                PostAllGamesInfo.postAllGamesToUser(requestingUser);
                PostAllGamesInfo.postAllGamesToLobby();
            } catch(Exception e){
                String response = "";
                System.out.println("Error when starting game: "+e);
                response = "{\"error\":\"Bad start game request:\n"+e+"\"}";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        }
    }
    //Returns a list of players connected to the websocket server
    static class ListPlayers implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            String response = User.getUsers();
            System.out.println("PlayerList Response:"+response);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
