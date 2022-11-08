package org.Game;

import java.util.Random;

public class GameController {


    Random random = new Random();

    int noPlayers = 2;
    int startMoney = 0;

    //Random player starts
    int playerTurn = random.nextInt(0, noPlayers);

    //Instantiates Player class depending on number of players
    Player[] players = new Player[noPlayers];


    //Makes players take turns in the consecutive order 0 through 4
    public void turn() {
        if (playerTurn < noPlayers){
            playerTurn += 1;
        } else {
            playerTurn = 0;
        }
    }

    //Starts game
    public void GameController(){

        //Sets starting balance in accordance to number of players
        if (noPlayers == 2){
            startMoney = 1000;
        }
        if (noPlayers == 3){
            startMoney = 2000;
        }
        if (noPlayers == 4){
            startMoney = 3000;
        }
        for (int i = 0; i < noPlayers; i++) {
            players[i] = new Player(startMoney, 0, "input fra spiller");
        }

            //Big ass loop begins
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

        turn(); //skifte-tur-metode
        players[playerTurn].getName(); //tager fat i specifik spiller


        //Game ends when a players balance <=0
        //The player with the highest balance wins
    }
}
