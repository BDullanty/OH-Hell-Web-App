package GameHandlers;

import java.util.HashMap;

public class GameHandler {

    //TODO: change to load from backend
    //Static:
    //Games are stored in a hashmap with Key as gameID
    private static HashMap<Integer, Game> lobbyGames = new HashMap<Integer, Game>();
    private static HashMap<Integer, Game> liveGames = new HashMap<Integer, Game>();

    private static HashMap<Integer, Game> finishedGames = new HashMap<Integer, Game>();
    //Static functions:
    public static Game getGame(int gameID){
        return lobbyGames.get(gameID);
    }
    public static void addGameToLobby(Game game){

        lobbyGames.put(game.getGameID(),game);
    }
    public static void moveGameToLive(Game game){
        lobbyGames.remove(game.getGameID());
        liveGames.put(game.getGameID(),game);
    }

    public static void moveGameToFinished(Game game){
        liveGames.remove(game.getGameID());
        finishedGames.put(game.getGameID(),game);
    }
}
