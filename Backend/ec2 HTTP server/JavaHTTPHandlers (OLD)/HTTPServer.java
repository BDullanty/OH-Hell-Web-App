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
        server.createContext("/JoinGame", new JoinGameHandler());
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
                PostUserInfo.postUserInfo(u);
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
                System.out.println("got user " + u.getUsername() +" and they have a gameID of "+u.getGameID());
                Game game = GameHandler.getGame(u.getGameID());

                GameHandler.removeUserFromGame(u);
                if(game.getPlayers().isEmpty()) GameHandler.end(game);

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
    static class JoinGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Received join game request"  );
            String response = "";
            try{
                String body = new String(exchange.getRequestBody().readAllBytes());
                System.out.println("Game leave body: "+body  );
                JSONObject infoJson = new JSONObject(body);
                User u = User.getUser(infoJson.getString("connectionID"));
                Game requestGame = GameHandler.getGame(infoJson.getInt("gameID"));
                System.out.println("got user " + u.getUsername() +"requesting into game "+requestGame.getGameID()+" and they have a gameID of "+u.getGameID());
                //check stuff
                if(!requestGame.getState().equals(State.WAITING)) throw new IllegalAccessError("Game is not joinable.");
                if(requestGame.getPlayers().size()==5) throw new IllegalAccessError("Full game.");

                if(u.getGameID() != -1) {
                    Game oldGame = GameHandler.getGame(u.getGameID());
                    GameHandler.removeUserFromGame(u);
                    if(oldGame.getPlayers().isEmpty()) GameHandler.end(oldGame);
                }
                GameHandler.addUserToGame(u,requestGame.getGameID());

                PostUserInfo.postUserInfo(u);
                PostAllGamesInfo.postAllGamesToLobby();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
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
                //Requires json to have gameID with gameID
                Game game = GameHandler.getGame(requestingUser.getGameID());

                System.out.println("Requested game "+game.getGameID()+" which has "+game.getPlayers().size()+" players, by user " + requestingUser.getUsername() );
                if( game.getState() != State.WAITING){
                    System.out.println("not in waiting state...." );
                    throw new IllegalAccessException("Requesting user "+requestingUser.getUsername()+" is trying to start a game that is not in waiting ");
                }
                if(requestingUser.hasVoted()){
                    System.out.println("Already voted" );
                    throw new IllegalAccessError("Already voted");
                }
                System.out.println("Setting user to voted...." );
                requestingUser.setVoted();
                System.out.println("Checking if everyone has voted...:");
                if(GameHandler.everyoneVotedStart(game)){

                    System.out.println("Everyone Has Voted");
                    GameHandler.start(game);

                    PostAllGamesInfo.postAllGamesToLobby();
                }
                else{
                    System.out.println("Still waiting on a vote in httpServer");
                    PostAllGamesInfo.postAllGamesToLobby();
                }
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

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
