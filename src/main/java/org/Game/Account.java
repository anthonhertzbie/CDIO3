package org.Game;

public class Account {

    private int playerBalance;

    public int getPlayerBalance() {
        return playerBalance;
    }

    public void setplayerbalanceDelete(int inputs){playerBalance = inputs;}

    public void setPlayerBalance(int playerBalance) {
        this.playerBalance += playerBalance;
    }
}
