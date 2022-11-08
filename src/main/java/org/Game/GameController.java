package org.Game;

public class GameController {

    public void startGame(){
        //Sets starting balance in accordance to number of players
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
        //Instantiates Player class depending on number of players
        Player[] players = new Player[noPlayers];
        for (int i = 0; i < noPlayers; i++) {
            players[i] = new Player(startMoney, 0, "Gui spiller navn");
        }
        //Random player starts
        //Player gets new position
        //If field type is a property and is unowned, the player buys the property
        //If field type ia a property and is owned, the player pays rent
        //If field type is a property and another player owns both, the player pays double rent
        //If field type is parking the turn ends
        //If field type is the prison
            //the player can use the jailcard
            //else the player can pay
            //else the player is not allowed to roll until the third round
        //If field type is chance
            //chance card is drawn
                //if chance card is var
                    //Specific option
        //Game ends when a players balance <=0
        //The player with the highest balance wins
    }
}
