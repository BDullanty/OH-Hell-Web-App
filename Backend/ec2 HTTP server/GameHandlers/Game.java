package GameHandlers;


import java.util.ArrayList;
import java.util.HashMap;
public class Game {
    //TODO: change to load from backend
    //Static:
    //Games are stored in a hashmap with Key as gameID
    private static HashMap<Integer, Game> lobbyGames = new HashMap<Integer, Game>();
    public static int IDTracker;
    //NonStatic:
    Deck deck;
    ArrayList<Player> players;
    Player host;
    HashMap<Player,ArrayList<Card>> playerHands;
    private int gameID;

    public Game(Player host){

        this.gameID = this.IDTracker;
        this.players.add(host);
        this.host = host;
        this.deck = new Deck();
        lobbyGames.put(this.gameID,this);

    }

}
