package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

import java.awt.*;

public class GUI_Controller {
    GUI gui;
    GUI_Car[] gui_car;
    GUI_Player[] gui_Player;
    GUI_Street[] fields = new GUI_Street[24];

    public void setCup(int dice1, int dice2){
        gui.setDice(dice1, dice2);
    }

    public void guiHelper(String language, GUI_Street[] fields){
        gui = new GUI(fields);
        Helper.lang = gui.getUserSelection("Language", "Dansk", "English");
        gui.close();
        gui = new GUI(fields);
    }
    String getUserButtonPressed(String message, String ... options){
        return gui.getUserButtonPressed(message, options);
    }
    String getUserString(String userinput){
        return gui.getUserString(userinput);
    }
    public void getShowMessage(String message){
        gui.showMessage(message);
    }
    public void setDices(int dice1, int dice2) {
        gui.setDice(dice1, dice2);
    }
    public void displayChanceCard(String card){
        gui.displayChanceCard(card);
    }
    public void setNumberOfPlayers(int players){
        gui_Player = new GUI_Player[players];
        gui_car = new GUI_Car[players];
    }
    public void createGUI_Player(int index, Player player){
        gui_Player[index] = new GUI_Player(player.getName(), player.getAccountBalance(), gui_car[index]);
        gui.addPlayer(gui_Player[index]);
    }

    public void createGUI_Car(int index, String name, int startMoney){
        gui_car[index] =new GUI_Car();
        //Makes the firs car red, the second car black and so on
        if(index==0){
        gui_car[index].setPrimaryColor(new Color(102,0,0));
        }else if(index==1){
        gui_car[index].setPrimaryColor(new Color(0,255,255));
        }else if(index==2){
        gui_car[index].setPrimaryColor(new Color(0,102,0));
        }else if(index==3){
        gui_car[index].setPrimaryColor(new Color(255,153,255));
        }
        //Places the gui-player-car on the board
        this.fields[0].setCar(gui_Player[index],true);
    }

    public GUI_Street getField(int currentField) {
        return fields[currentField];
    }
    public void setGui_car(int player, int field, int prevfield){
        this.fields[field].setCar(this.gui_Player[player], true);
    }
    public GUI_Street[] createGameBoard(){
        for (int i = 0; i < 24; i++){
            Field field = new Field();
            fields[i] = field.getField("_Fields", i);
        }
        return fields;
    }
}
