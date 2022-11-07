package org.Game;

public class GameController {

    public void startGame(){

        int noPlayers = 2;
        int startMoney = 0;

        if (noPlayers == 2){
            startMoney = 1000;
        }
        if (noPlayers == 3){
            startMoney = 2000;
        }
        if (noPlayers == 4){
            startMoney = 3000;
        }
        Player[] players = new Player[noPlayers];
        for (int i = 0; i < noPlayers; i++) {
            players[i] = new Player(startMoney, 0, "Gui spiller navn");

        }
    }
}
