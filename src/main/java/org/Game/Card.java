package org.Game;

public class Card {
    private String cardDescription;
    private int index;

    public Card(int index, String cardDescription){
        this.index = index;
        this.cardDescription = cardDescription;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public int getIndex(){
        return index;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        builder.append(getCardDescription());
        return builder.toString();
    }
}
