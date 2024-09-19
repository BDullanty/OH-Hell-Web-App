package GameHandlers;


import java.util.ArrayList;
import java.util.HashMap;
public class Game {
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
        GameHandler.addGameToLobby(this);

    }
    public int getGameID(){
        return this.gameID;
    }

    public void startGame(){

    }
    public void endGame(){

    }

}
