package GameHandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    //
    private Stack<Card> deck;


    public Deck(){
        loadDeck();
        shuffle();
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }
    @Override
    public String toString() {
        String returnString = "";
        boolean first = true;
        for (Card card : deck) {
            if (first) {
                first = false;
                returnString += card.toString();
            }
            returnString += ", " + card.toString();
        }
        return returnString;
    }
    private void loadDeck(){
        this.deck = new Stack<>();
        for(int i = 1; i <=52;i++){
            deck.add(new Card(i));
        }
    }
}
