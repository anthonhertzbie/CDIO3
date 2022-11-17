package org.Game;

import gui_fields.GUI_Field;
import gui_fields.GUI_Street;

public class GameBoard {
    Field field = new Field();
    String[] fields = new String[24];

    public String[] getFields() {
        return fields;
    }
    public void createGameBoard(){
        for (int i = 0; i < fields.length; i++){
            fields[i] = field.getField("_Fields", i);
        }
    }
}
