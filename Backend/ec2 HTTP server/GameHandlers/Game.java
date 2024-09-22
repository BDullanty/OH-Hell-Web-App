package GameHandlers;


import java.util.ArrayList;
import java.util.HashMap;
public class Game {
    public static int IDTracker = 0;

    //NonStatic:
    private int gameID;
    private ArrayList<Player> players;
    private Enum state;
    private Deck deck;

    private int playerTurn;
    private User host;

    private HashMap<Player,ArrayList<Card>> playerHands;
    private int[] bets;
    private int round;


    public static int getIDTracker() {
        return IDTracker;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public User getHost() {
        return host;
    }

    public HashMap<Player, ArrayList<Card>> getPlayerHands() {
        return playerHands;
    }

    public int[] getBets() {
        return bets;
    }

    public Game(User host){

        this.gameID = ++this.IDTracker;
        this.players = new ArrayList<>();
        this.host = host;
        this.players.add(host);
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
        System.out.println("Changed game state to " + state);
        this.state= state;
        //set user states now
        if(state.equals(State.LOBBY)) state = State.WAITING;
        if(state.equals(State.COMPLETED)) state = State.LOBBY;
        for(int i = 0; i <players.size();i++){
            if(this.players.get(i).getClass().getSimpleName().equals("User")){
                User user = (User) this.players.get(i);
                user.setState(state);
                System.out.println("Set "+ user.getUsername()+ "'s state to "+ state);
            }
        }

    }

    public int getRound() {
        return round;
    }

    public Enum getState() {
        return state;
    }
}
