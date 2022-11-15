package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;

import java.awt.*;

public class Player {
    private int playerPosition;

    private int index;
    Account acc = new Account();
    GUI_Player gui_Player;
    GUI_Car gui_car;
    private boolean jail;

    private boolean hasCard;

    private boolean jailCard;

    public GUI_Player getGui_Player() {
        return gui_Player;
    }

    public Player (int index, int startMoney, int playerPosition, String name, GUI_Field field){
        this.playerPosition = playerPosition;
        acc.setPlayerBalance(startMoney);
        // Creates car and player in the gui
        gui_car = new GUI_Car();
        gui_Player = new GUI_Player(name, startMoney, gui_car);
        //Makes the firs car red, the second car black and so on
        if(index == 0){
            gui_car.setPrimaryColor(Color.RED);
        } else if(index == 1){
            gui_car.setPrimaryColor(Color.yellow);
        } else if(index == 2){
            gui_car.setPrimaryColor(Color.green);
        } else if(index == 3){
            gui_car.setPrimaryColor(Color.blue);
        }
        //Places the gui-player-car on the board
        field.setCar(gui_Player,true);
        this.index = index;
        this.jail = false;
        this.hasCard = false;
        this.jailCard = false;
    }

    public int getIndex(){
        return this.index;
    }

    public void addPlayerPosition(int diceThrow, GUI_Field field, GUI_Field prevField) {
        //Removes previous version of car-placement on the board
        prevField.setCar(gui_Player, false);
        this.playerPosition += diceThrow;
        //Just places the car in gui at the new position
        field.setCar(gui_Player, true);
    }

    public void setPlayerPosition(int playerPosition, GUI_Field field, GUI_Field prevField) {
        //Removes previous version of car-placement on the board
        prevField.setCar(gui_Player, false);
        this.playerPosition = playerPosition;
        //Just places the car in gui at the new position
        field.setCar(gui_Player, true);
    }

    public int getAccountBalance() {
        return acc.getPlayerBalance();
    }

    public void addAccountBalance(int acc) {
        this.acc.setPlayerBalance(acc);
        this.gui_Player.setBalance(this.acc.getPlayerBalance());
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
    public String getName(){
        return gui_Player.getName();

    }
}
