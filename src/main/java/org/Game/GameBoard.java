package org.Game;

import gui_fields.GUI_Field;
import gui_fields.GUI_Street;

public class GameBoard {
    public GUI_Field[] field2 = new GUI_Field[24];
    public GUI_Street[] fields = new GUI_Street[24];
    Field field = new Field();


    public GUI_Street getField(int currentField) {
        return fields[currentField];
    }

    public GUI_Street[] createGameBoard(){
        for (int i = 0; i < 24; i++){
            fields[i] = field.getField("_Fields", i);
        }
        return fields;
    }
}
