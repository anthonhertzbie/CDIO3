package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameController {

    Random random = new Random();
    Cup cup = new Cup();
    GUI_Street street = new GUI_Street();
    GameBoard gameBoard = new GameBoard();
    GUI gui = new GUI();

    List<GUI_Car> cars = new ArrayList<>();
    List<GUI_Player> gui_players = new ArrayList<>();

    int noPlayer = 2;
    int startMoney = 0;

    //Random player starts
    int playerTurn = random.nextInt(0, noPlayer);

    //Instantiates Player class depending on number of players
    Player[] player = new Player[noPlayer];

    //Makes players take turns in the consecutive order 0 through 4
    public void turn() {
        if (playerTurn < noPlayer){
            playerTurn += 1;
        } else {
            playerTurn = 0;
        }
    }

    //Starts game
    public void startGame(){
        //Sets starting balance in accordance to number of players
        if (noPlayer == 2){
            startMoney = 1000;
        }
        if (noPlayer == 3){
            startMoney = 2000;
        }
        if (noPlayer == 4){
            startMoney = 3000;
        }
        for (int i = 0; i < noPlayer; i++) {
            player[i] = new Player(startMoney, 0, gui.getUserString("Enter player name: "));
        }


        while (true) {
            gui.showMessage(player[playerTurn].getName() + "'s turn. Press OK to roll");
            cup.rollDices();
            gui.setDice(cup.getDice1(), cup.getDice2());
            gameBoard.toString();


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



        //Game ends when a players balance <=0
        //The player with the highest balance wins
    }
}
