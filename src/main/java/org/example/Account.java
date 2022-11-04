package org.example;

public class Account {

    private int playerBalance;

    public int getPlayerBalance() {
        return playerBalance;
    }

    public void setPlayerBalance(int playerBalance) {
        this.playerBalance += playerBalance;
    }
}
