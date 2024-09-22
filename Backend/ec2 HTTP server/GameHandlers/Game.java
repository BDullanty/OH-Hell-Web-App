package GameHandlers;


import java.util.ArrayList;
import java.util.HashMap;
public class Game {
    public static int IDTracker;
    //NonStatic:
    private int gameID;
    private ArrayList<Player> players;
    private Enum state;
    private Deck deck;

    private int playerTurn;
    private Player host;

    private HashMap<Player,ArrayList<Card>> playerHands;
    private int[] bets;


    public Game(Player host){

        this.gameID = this.IDTracker;
        this.players.add(host);
        this.host = host;
        this.deck = new Deck();
        this.state = State.LOBBY;
        this.playerTurn = 0;
        this.bets = new int[5];
        this.playerHands = new HashMap<>();
        GameHandler.addGameToLobby(this);

    }
    public int getGameID(){
        return this.gameID;
    }
    public void addPlayer(Player p){
        this.players.add(p);
    }
    public void removePlayer(Player p){
        this.players.remove(p);
        if(this.players.isEmpty()) endGame();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void startGame(){
        GameHandler.start(this);
    }
    public void endGame(){
        GameHandler.end(this);
    }
    public void setState(State state) {
        this.state= state;
    }

    public int getRound() {
        return round;
    }

    public Enum getState() {
        return state;
    }
}
