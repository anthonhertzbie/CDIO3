package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Chance;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {


    Random random = new Random();
    Cup cup = new Cup();
    GUI_Street street = new GUI_Street();
    GameBoard gameBoard = new GameBoard();
    Deck deck = new Deck();
    boolean jail = false;



    int noPlayer = 2;
    int startMoney = 0;

    //Random player starts
    int playerTurn = random.nextInt(0, noPlayer);

    //Instantiates Player class depending on number of players
    Player[] player = new Player[noPlayer];

    GUI_Player[] players = new GUI_Player[noPlayer];
    GUI_Car[] cars = new GUI_Car[noPlayer];

    //Makes players take turns in the consecutive order 0 through 4
    public void turn() {
        if (playerTurn < noPlayer - 1){
            playerTurn += 1;
        } else {
            playerTurn = 0;
        }
    }



    //Starts game
    public void startGame(){
        GUI gui = new GUI(gameBoard.gameBoard());

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
            cars[i] = new GUI_Car();
            players[i] = new GUI_Player(player[i].getName(), player[i].getAccountBalance(), cars[i]);
            gui.addPlayer(players[i]);
            gameBoard.getField(0).setCar(players[i],true);
        }


        while (true) {
            gui.showMessage(player[playerTurn].getName() + "'s turn. Press OK to roll");
            System.out.println(gameBoard.getField(0).getRent());
            gameBoard.getField(player[playerTurn].getPlayerPosition()).setCar(players[playerTurn], false);
            cup.rollDices();
            gui.setDice(cup.getDice1(), cup.getDice2());

            if (player[playerTurn].getPlayerPosition()+cup.getSum() < 24){
                player[playerTurn].addPlayerPosition(cup.getSum());
            } else {
                player[playerTurn].addPlayerPosition(cup.getSum()-24);
            }


            gameBoard.getField(player[playerTurn].getPlayerPosition()).setCar(players[playerTurn], true);

            // Bør være et if/else hvor man får rent*2 hvis en spiller ejer 2 grunde
            player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
            players[playerTurn].setBalance(player[playerTurn].getAccountBalance());

            if (player[playerTurn].getPlayerPosition() == 3 ||player[playerTurn].getPlayerPosition() == 9 || player[playerTurn].getPlayerPosition() == 15 || player[playerTurn].getPlayerPosition() == 21){
                // Write something with chance-cards.
                gui.displayChanceCard(deck.toString());
            }


            turn();





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
