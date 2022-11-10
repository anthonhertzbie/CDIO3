package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;

import java.awt.*;

public class Player {
    private int playerPosition;
    String name;
    Account acc = new Account();
    private int noPlayer = 2;
    GUI_Player[] gui_players = new GUI_Player[noPlayer];
    GUI_Car[] cars = new GUI_Car[noPlayer];

    public void createPlayers(int i, int startMoney, String name, GUI_Field field){
        // Creates car and player in the gui
        cars[i] = new GUI_Car();
        gui_players[i] = new GUI_Player(name, startMoney, cars[i]);
        //Makes the firs car red, the second car black and so on
        if(i == 0){
            cars[i].setPrimaryColor(Color.RED);
        } else if(i == 1){
            cars[i].setPrimaryColor(Color.black);
        } else if(i == 2){
            cars[i].setPrimaryColor(Color.green);
        } else if(i == 3){
            cars[i].setPrimaryColor(Color.blue);
        }
        //Places the gui-player-car on the board
        field.setCar(gui_players[i],true);
    }

    public GUI_Player[] getGui_players() {
        return gui_players;
    }

    public Player (int startMoney, int playerPosition, String navn){
        this.playerPosition = playerPosition;
        this.name = navn;
        acc.setPlayerBalance(startMoney);
    }

    public void addPlayerPosition(int i, int diceThrow, GUI_Field field) {

        //Removes previous version of car-placement on the board
        field.setCar(gui_players[i], false);
        this.playerPosition += diceThrow;
        //Just places the car in gui at the new position
        field.setCar(gui_players[i], true);
        field.setBackGroundColor(gui_players[i].getPrimaryColor());
    }
    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public int getAccountBalance() {
        return acc.getPlayerBalance();
    }

    public void setAccountBalance(int acc) {
        this.acc.setPlayerBalance(acc);
        this.gui_players[0].setBalance(this.acc.getPlayerBalance());
        this.acc.setPlayerBalance(acc);
    }

    public int getPlayerPosition(){
        return playerPosition;
    }
    public String getName(){
        return name;

    }
}
