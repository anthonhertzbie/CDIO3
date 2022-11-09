package org.Game;

import java.util.Random;

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

    public void shuffle() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int a = random.nextInt(0, 20);
            int b = random.nextInt(0, 20);
            Card savedcard = card[a];
            card[a] = card[b];
            card[b] = savedcard;
        }
    }

    public Card draw() {
        Card firstcard = card[0];
        for (int i = 0; i < card.length - 1; i++) {
            card[i] = card[i+1];
        }
        card[card.length - 1] = firstcard;
        return firstcard;
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
