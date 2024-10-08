package GameHandlers;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler {



    //TODO: change to load from backend
    //Static:
    //Games are stored in a hashmap with Key as gameID
    private static HashMap<Integer, Game> games = new HashMap<Integer, Game>();
    private static HashMap<Integer, Game> history = new HashMap<Integer, Game>();


    //Static functions:
    public static Game getGame(int gameID){
        Game resultGame = games.get(gameID);
        System.out.println("Result from getGame: passed id: "+ gameID+" resultantID: "+resultGame.getGameID());
        return resultGame;
    }

    public static void start(Game game){
        //to start game, we will set the state to in game
        System.out.println("Starting Game");
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

        System.out.println("Moving game to lobby: "+game.getGameID());
        games.put(game.getGameID(),game);
        game.setState(State.WAITING);
    }
    public static void end(Game game){
        System.out.println("Ending game "+game.getGameID());
        boolean doHistoryStore = true;
        if(game.getState().equals(State.WAITING)) doHistoryStore = false;
        games.remove(game.getGameID());
        //to start game, we will set the state to in game
        game.setState(State.COMPLETED);
        if(doHistoryStore) history.put(game.getGameID(),game);
        for(Player p : game.getPlayers()){
            removeUserFromGame(p);
        }

    }
    public static void removeUserFromGame(Player u) {
        Game game = GameHandler.getGame(u.getGameID());
        game.removePlayer(u);
        u.setGameID(-1);
        u.setState(State.LOBBY);
        u.unsetVote();
        System.out.println(  u.getUsername()+ " left game "+game.getGameID());

    }
    public static void addUserToGame(User u,int gameID) {
        Game game = GameHandler.getGame(gameID);
        game.addPlayer(u);
        System.out.print(u.getUsername() +" added to game "+game.getGameID());
    }

    public static boolean everyoneVotedStart(Game game){
        boolean ready = true;
        for(Player p : game.getPlayers()){
            if(!p.hasVoted()) {

                System.out.println("Still waiting for vote from "+p.getUsername()+" in game "+game.getGameID() );
                ready = false;
            }
            else{
                System.out.println(p.getUsername()+" has voted");
            }
        }
        return ready;
    }
    public static String getLobbyGamesJson() {

        System.out.println("There are "+games.size()+" lobbies");
        String gameString = "{";
        int tracker = 0;
        for(Game game : games.values()){
            tracker+=1;

            ArrayList<Player> players = game.getPlayers();
            gameString+= " \""+game.getGameID()+"\" : {";

            gameString+= "\"host\":\""+players.get(0).username+"\",";
            int size = players.size();
            for(int j = 0; j < size;j++){
                gameString+= "\"player"+(j+1)+"\":\""+players.get(j).username+"\", ";
            }
            //size = 2
            //so we iterate 1,2,3+size for playernum
            for(int k =1; k <= 5-size;k++){

                gameString+= "\"player"+(k+size)+"\":\"Empty\", ";

            }
            gameString+= "\"state\":\""+game.getState()+"\",";
            gameString+= "\"round\":\""+game.getRound()+"\"";

            if(tracker !=games.size()) gameString+="},";
            else gameString+="}";
            //for each game, we want to format
        }
        gameString+="}";
        return gameString;
    }


}
