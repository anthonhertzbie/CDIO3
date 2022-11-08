package org.Game;

public class Card {
    private String cardDescription;

    public Card(String cardDescription){
        this.cardDescription = cardDescription;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        builder.append(getCardDescription());
        return builder.toString();
    }
}
