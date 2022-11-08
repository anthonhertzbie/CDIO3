package org.Game;

import gui_fields.GUI_Field;
import gui_fields.GUI_Street;

import java.awt.*;

public class GameBoard {
    public GUI_Field[] field = new GUI_Field[24];


    public GUI_Field getField(int currentField) {
        return field[currentField];
    }

    public GUI_Field[] gameBoard(){
        field[0] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[1] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[2] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[3] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[4] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[5] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[6] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[7] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[8] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[9] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[10] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[11] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[12] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[13] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[14] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[15] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[16] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[17] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[18] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[19] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[20] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[21] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[22] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        field[23] = new GUI_Street("Start", "", "", "-50", Color.WHITE, Color.BLACK);
        return field;
    }
}
