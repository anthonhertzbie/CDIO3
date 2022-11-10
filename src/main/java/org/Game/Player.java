package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;

import java.awt.*;

public class Player {
    private int playerPosition;
    Account acc = new Account();
    GUI_Player gui_Player;
    GUI_Car gui_car;

    public GUI_Player getGui_Player() {
        return gui_Player;
    }

    public Player (int i, int startMoney, int playerPosition, String name, GUI_Field field){
        this.playerPosition = playerPosition;
        acc.setPlayerBalance(startMoney);
        // Creates car and player in the gui
        gui_car = new GUI_Car();
        gui_Player = new GUI_Player(name, startMoney, gui_car);
        //Makes the firs car red, the second car black and so on
        if(i == 0){
            gui_car.setPrimaryColor(Color.RED);
        } else if(i == 1){
            gui_car.setPrimaryColor(Color.yellow);
        } else if(i == 2){
            gui_car.setPrimaryColor(Color.green);
        } else if(i == 3){
            gui_car.setPrimaryColor(Color.blue);
        }
        //Places the gui-player-car on the board
        field.setCar(gui_Player,true);
    }

    public void addPlayerPosition(int diceThrow, GUI_Field field, GUI_Field prevField) {
        //Removes previous version of car-placement on the board
        prevField.setCar(gui_Player, false);
        this.playerPosition += diceThrow;
        //Just places the car in gui at the new position
        field.setCar(gui_Player, true);
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public int getAccountBalance() {
        return acc.getPlayerBalance();
    }

    public void setAccountBalance(int acc) {
        this.acc.setPlayerBalance(acc);
        this.gui_Player.setBalance(this.acc.getPlayerBalance());
    }

    public int getPlayerPosition(){
        return playerPosition;
    }
    public String getName(){
        return gui_Player.getName();

    }
}
