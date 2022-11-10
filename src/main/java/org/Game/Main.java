package org.Game;

import static org.Game.Helper.lang;

public class Main {
    public static void main(String[] args) {
        lang = "Dansk";
        Deck deck = new Deck();

        GameController gameController = new GameController();
        gameController.startGame();
    }
}