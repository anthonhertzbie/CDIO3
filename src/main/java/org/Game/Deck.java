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
            card[i] = new Card(helper.lineReader("_Deck", i));
        }

    }
    public String toString(){
        StringBuilder cards = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            cards.append(card[i].toString());
            if(i < 19) {
                cards.append("\n");
            }
        }
        return cards.toString();
    }



}
