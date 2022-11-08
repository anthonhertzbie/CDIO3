package org.Game;

public class GameBoard {
    private Field[] field;


    public GameBoard() {
        Field[] field = new Field[24];
        initGameBoard();
    }

    private void initGameBoard() {
        Helper helper = new Helper();
        for (int i = 0; i < field.length; i++) {
            field[i] = new Field(i, helper.lineReader("_Fields", i));
        }
    }
}
