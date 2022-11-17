package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;

public class Player {
    private int playerPosition;

    Account acc = new Account();
    private boolean jail;

    private boolean hasCard;

    private boolean jailCard;

    private int field;

    String name;

    public String getName() {
        return name;
    }

    public Player(int startMoney, int playerPosition, String name){
        this.playerPosition = playerPosition;
        acc.setPlayerBalance(startMoney);
        // Creates car and player in the gui
        this.jail = false;
        this.hasCard = false;
        this.jailCard = false;
        this.field = 0;
        this.name = name;
    }

    public void setField(int field) {
        this.field = field;
    }

    public int getField() {
        return field;
    }

    public void addPlayerPosition(int diceThrow) {
        //Removes previous version of car-placement on the board
        if(playerPosition + diceThrow > 24){
            playerPosition = this.playerPosition - 24;
            playerPosition += diceThrow;
        } else {
            playerPosition += diceThrow;
        }
        //Just places the car in gui at the new position
    }

    public void setPlayerPosition(int playerPosition) {
        //Removes previous version of car-placement on the board
        this.playerPosition = playerPosition;
    }

    public int getAccountBalance() {
        return acc.getPlayerBalance();
    }

    public void addAccountBalance(int acc) {
        this.acc.setPlayerBalance(acc);
      //  this.gui_Player.setBalance(this.acc.getPlayerBalance());
    }

    public void setJail(boolean jail) {
        this.jail = jail;
    }
    public boolean getJail(){
        return this.jail;
    }

    public void setHasCard(boolean hasCard) {
        this.hasCard = hasCard;
    }
    public boolean getHasCard(){
        return this.hasCard;
    }

    public void setJailCard(boolean jailCard){
        this.jailCard = jailCard;
    }

    public boolean getJailCard(){
        return this.jailCard;
    }


    public int getPlayerPosition(){
        return playerPosition;
    }
  //  public String getName(){
  //      return gui_Player.getName();


}
