package GameHandlers;

import java.util.HashMap;

public class GameHandler {

    public static String getLobbyGames() {
        return lobbyGames.values().toString();
    }


    //TODO: change to load from backend
    //Static:
    //Games are stored in a hashmap with Key as gameID
    private static HashMap<Integer, Game> games = new HashMap<Integer, Game>();


    //Static functions:
    public static Game getGame(int gameID){
        return games.get(gameID);
    }

    public static void start(Game game){
        //to start game, we will set the state to in game
        game.setState(State.INGAME);
        //TODO:
        //We must add in bot players for remaining slots
        //We will loop n rounds, each round players get cards, bet, and do sub rounds up to n
        //We have subrounds where players play cards
        //Host will lead the first bet round.
        //each sub round will:
        // Have a player play a card.
        //After each sub round, we will clean up, and do next round.


        //Each game must track:
        //Whos turn it is
        //What bets were taken
        //What hands they have
        //

    }
    public static void bet(Player p, int bet,Game game){


    }
    public static void addGameToLobby(Game game){
        games.put(game.getGameID(),game);
        game.setState(State.LOBBY);
    }
    public static void end(Game game){

        game.setState(State.COMPLETED);
        //to start game, we will set the state to in game

    }

}
