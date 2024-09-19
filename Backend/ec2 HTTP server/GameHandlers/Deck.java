package GameHandlers;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;

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
}
