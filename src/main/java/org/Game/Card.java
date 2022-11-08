package org.Game;

public class Card {
    private int cardNumber;
    private String cardDescription;

    public Card(int cardNumber, String cardDescription){
        this.cardNumber = cardNumber;
        this.cardDescription = cardDescription;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(getCardNumber());
        builder.append(" ");
        builder.append(getCardDescription());
        return builder.toString();
    }
}
