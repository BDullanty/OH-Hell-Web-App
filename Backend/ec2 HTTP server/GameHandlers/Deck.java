package GameHandlers;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;

    public void shuffle(){
        Collections.shuffle(deck);
    }
}
