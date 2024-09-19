package GameHandlers;

public class Card {
    private int ID;
    private int value;
    private Enum suit;

    public Card(int ID){

        this.ID=ID;
        this.value = getValueFromID(ID);
        this.suit = getSuitFromID(ID);

    }
    private int getValueFromID(int id){
        //We have 52 cards from 1-52, with 13 per suit.
        //this means we can loop from 1-52, having the ID be equal to the number,
        //The value equal to n%12 (with 12 valued at 12 and 1 valued at 13, (ace high)
        if(id%12 ==1) return 13;
        if(id%12 == 0) return 12;
        return id%12;
    }
    private Enum getSuitFromID(int ID){
        //We have each 13 cards as a suit,
        if(ID <=13 && ID >=1){
            return Suits.HEART;
        } else if(ID>13 && ID <= 26){
            return Suits.DIAMOND;
        } else if(ID> 26 && ID<=39 ){
            return Suits.SPADE;
        } else{
            return Suits.CLUB;
        }
    }
}
