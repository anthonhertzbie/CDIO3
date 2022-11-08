package org.Game;

public class GameBoard {
    private Field[] field = new Field[24];

    public GameBoard() {
        initGameBoard();
    }

    private void initGameBoard() {

        field[0] = new Field(0, " ");
        field[1] = new Field(1, " ");
        field[2] = new Field(2, " ");
        field[3] = new Field(3, " ");
        field[4] = new Field(4, " ");
        field[5] = new Field(5, " ");
        field[6] = new Field(6, " ");
        field[7] = new Field(7, " ");
        field[8] = new Field(8, " ");
        field[9] = new Field(9, " ");
        field[10] = new Field(10, " ");
        field[11] = new Field(11, " ");
        field[12] = new Field(12, " ");
        field[13] = new Field(13, " ");
        field[14] = new Field(14, " ");
        field[15] = new Field(15, " ");
        field[16] = new Field(16, " ");
        field[17] = new Field(17, " ");
        field[18] = new Field(18, " ");
        field[19] = new Field(19, " ");
        field[20] = new Field(20, " ");
        field[21] = new Field(21, " ");
        field[22] = new Field(22, " ");
        field[23] = new Field(23, " ");

    }
}
