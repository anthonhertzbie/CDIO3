package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

import java.awt.*;

public class GUI_Controller {
    GUI gui;
    GUI_Car[] gui_car;
    GUI_Street field;
    GUI_Player[] gui_Player;
    GUI_Street[] fields = new GUI_Street[24];

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
    public void setGUI_NumberOfPlayersAndCars(int players){
        gui_Player = new GUI_Player[players];
        gui_car = new GUI_Car[players];
    }
    public void createGUI_Player(int index, Player player){
        gui_Player[index] = new GUI_Player(player.getName(), player.getAccountBalance(), gui_car[index]);
        //Places the gui-player-car on the board
        this.fields[0].setCar(gui_Player[index],true);
        gui.addPlayer(gui_Player[index]);
    }

    public void createGUI_Car(int index, String name, int startMoney){
        gui_car[index] = new GUI_Car();
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
    }

    public GUI_Street getField(int currentField) {
        return fields[currentField];
    }
    public void setGui_car(int player, int field, int prevfield){
        this.fields[prevfield].setCar(this.gui_Player[player], false);
        this.fields[field].setCar(this.gui_Player[player], true);
    }
    public GUI_Street[] createGameBoard(String[] fields){
        int R1, G1, B1;
        Color color;
        String[] fieldInformation;
        for (int i = 0; i < 24; i++){
            fieldInformation = fields[i].split("/");
        // Stolen from https://stackoverflow.com/questions/2854043/converting-a-string-to-color-in-java, Erick Robertson && ZZ Coder
        try {
            java.lang.reflect.Field colorReflect = Class.forName("java.awt.Color").getField(fieldInformation[5]);
            color = (Color)colorReflect.get(null);
        } catch (Exception e) {
            color = null; // Not defined
        }
        // Stealing stops
        R1 = Integer.parseInt(fieldInformation[4].split(",")[0]);
        G1 = Integer.parseInt(fieldInformation[4].split(",")[1]);
        B1 = Integer.parseInt(fieldInformation[4].split(",")[2]);
        System.out.println(R1);
        System.out.println(G1);
        System.out.println(B1);


        System.out.println(fieldInformation[5]);
        System.out.println(fieldInformation[0] + " || "+ fieldInformation[1]+ " || "+ fieldInformation[2]+ " || "+ fieldInformation[3]+ " || "+ fieldInformation[4]+ " || "+ fieldInformation[5]);
        this.fields[i] = new GUI_Street(fieldInformation[0], fieldInformation[1], fieldInformation[2], fieldInformation[3], new Color(R1,G1,B1), color);
        this.fields[i].setBorder(Color.BLACK, Color.white);


        }
        return this.fields;
    }
}
