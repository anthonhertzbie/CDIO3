package org.Game;

import java.util.Arrays;

public class Player {
    private int playerPosition;
    String name;
    private Account acc = new Account();

    public Player (int startMoney, int playerPosition, String navn){
        this.playerPosition = playerPosition;
        this.name = navn;
        acc.setPlayerBalance(startMoney);
    }

    public void addPlayerPosition(int diceThrow, int money) {
        this.playerPosition += diceThrow;
        acc.setPlayerBalance(money);
    }

    public void setPlayerPosition(int playerPosition, int money) {
        this.playerPosition = playerPosition;
        acc.setPlayerBalance(money);
    }

    public int getAccountBalance() {
        return acc.getPlayerBalance();
    }
    public int getPlayerPosition(){
        return playerPosition;
    }
    public String getName(){
        return name;

    }
}
