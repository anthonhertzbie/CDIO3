package org.Game;

import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

public class GUI_Controller {
    GUI gui;
    Helper helper = new Helper();
    public void guiHelper(String language, GUI_Street[] fields){
        gui = new GUI(fields);
        helper.lang = gui.getUserSelection("Language", "Dansk", "English");
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
    public void addPlayer(GUI_Player name){
        gui.addPlayer(name);
    }
}
