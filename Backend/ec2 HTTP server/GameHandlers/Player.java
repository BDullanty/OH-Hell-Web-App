package GameHandlers;

import java.util.ArrayList;
import java.util.HashMap;
//Can be a bot or a user
public abstract class Player{


    protected String username;
    protected int gameID;
    protected ArrayList<Card> hand;

    protected int bet;

    boolean hasVoted;
    public Player(String username) {
        this.username = username;
        this.gameID = -1;
        this.hand = new ArrayList<>();
        this.hasVoted = false;
    }


    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        gameID = gameID;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }


    public boolean hasVoted() {
        return this.hasVoted;
    }


}
