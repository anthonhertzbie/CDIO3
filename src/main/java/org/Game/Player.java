package org.Game;

public class Player {
    private int playerPosition;
    String name;
    private Account acc = new Account();

    public Player (int startMoney, int playerPosition, String navn){
        this.playerPosition = playerPosition;
        this.name = navn;
        acc.setPlayerBalance(startMoney);
    }

    public void addPlayerPosition(int diceThrow) {
        this.playerPosition += diceThrow;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public int getAccountBalance() {
        return acc.getPlayerBalance();
    }

    public void setAccountBalance(int acc) {
        this.acc.setPlayerBalance(acc);
    }

    public int getPlayerPosition(){
        return playerPosition;
    }
    public String getName(){
        return name;

    }
}
