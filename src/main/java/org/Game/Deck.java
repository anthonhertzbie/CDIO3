package org.Game;

import static org.Game.Helper.lang;

public class Deck {

    private Card[] card = new Card[20];

    /**
     * Deck constructor with 24 cards
     */
    public Deck() {
        initDeck();
    }

    /**
     * The initializer for the specific chance cards
     */
    private void initDeck() {
        Helper helper = new Helper();
        for (int i = 0; i < card.length; i++) {
            card[i] = new Card(helper.lineReader(i));
        }

    }
    public String toString(){
        StringBuilder kortspillet = new StringBuilder();
        kortspillet.append("[");
        for (int i = 0; i < 20; i++) {
            kortspillet.append(card[i].toString());
            if(i < 19) {
                kortspillet.append(", \n");
            }
        }
        kortspillet.append("]");
        return kortspillet.toString();
    }



}
