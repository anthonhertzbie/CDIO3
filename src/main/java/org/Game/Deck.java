package org.Game;

import static org.Game.Helper.lang;

public class Deck {

    private Card[] card = new Card[24];

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
        card[0] = new Card(0, helper.lineReader(1));
        card[1] = new Card(1, " ");
        card[2] = new Card(2, " ");
        card[3] = new Card(3, " ");
        card[4] = new Card(4, " ");
        card[5] = new Card(5, " ");
        card[6] = new Card(6, " ");
        card[7] = new Card(7, " ");
        card[8] = new Card(8, " ");
        card[9] = new Card(9, " ");
        card[10] = new Card(10, " ");
        card[11] = new Card(11, " ");
        card[12] = new Card(12, " ");
        card[13] = new Card(13, " ");
        card[14] = new Card(14, " ");
        card[15] = new Card(15, " ");
        card[16] = new Card(16, " ");
        card[17] = new Card(17, " ");
        card[18] = new Card(18, " ");
        card[19] = new Card(19, " ");
        card[20] = new Card(20, " ");
        card[21] = new Card(21, " ");
        card[22] = new Card(22, " ");
        card[23] = new Card(23, " ");
    }
    public String toString(){
        StringBuilder kortspillet = new StringBuilder();
        kortspillet.append("[");
        for (int i = 0; i < 24; i++) {
            kortspillet.append(card[i].toString());
            if(i < 23) {
                kortspillet.append(", ");
            }
        }
        kortspillet.append("]");
        return kortspillet.toString();
    }



}
