package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.util.Random;

public class GameController {


    Random random = new Random();
    Cup cup = new Cup();
    GameBoard gameBoard = new GameBoard();
    Deck deck = new Deck();
    boolean jail = false;



    int noPlayer = 2;
    int startMoney = 0;

    //Random player starts
    int playerTurn = random.nextInt(0, noPlayer);

    //Instantiates Player class depending on number of players
    Player[] player = new Player[noPlayer];

    GUI_Player[] gui_players = new GUI_Player[noPlayer];
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
            startMoney = 20000000;
        }
        if (noPlayer == 3){
            startMoney = 18000000;
        }
        if (noPlayer == 4){
            startMoney = 16000000;
        }
        for (int i = 0; i < noPlayer; i++) {
            player[i] = new Player(startMoney, 0, gui.getUserString("Enter player name: "));
            cars[i] = new GUI_Car();
            //Makes the gui-player and determines the players attributes
            gui_players[i] = new GUI_Player(player[i].getName(), startMoney, cars[i]);
            gui.addPlayer(gui_players[i]);
            //Places the gui-player-car on the board
            gameBoard.getField(0).setCar(gui_players[i],true);
        }


        while (true) {
            //Important note: the game stops until "OK" is pressed in the GUI
            gui.showMessage(player[playerTurn].getName() + "'s turn. Press OK to roll");
            //Removes previous version of car-placement on the board
            gameBoard.getField(player[playerTurn].getPlayerPosition()).setCar(gui_players[playerTurn], false);
            cup.rollDices();
            gui.setDice(cup.getDice1(), cup.getDice2());

            //Loop that makes the players go around in circle instead of breaking at field 24
            if (player[playerTurn].getPlayerPosition()+cup.getSum() < 24){
                player[playerTurn].addPlayerPosition(cup.getSum());
            } else {
                System.out.printf("Not counting start");
                player[playerTurn].addPlayerPosition(cup.getSum()-24);
                player[playerTurn].setAccountBalance(20000000);
                gui_players[playerTurn].setBalance(player[playerTurn].getAccountBalance());
                if (player[playerTurn].getPlayerPosition() == 18){
                    player[playerTurn].setAccountBalance(-20000000);
                }
            }

            //Just places the car in gui at the new position
            gameBoard.getField(player[playerTurn].getPlayerPosition()).setCar(gui_players[playerTurn], true);

            // Bør være et if/else hvor man får rent*2 hvis en spiller ejer 2 grunde
            player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
            gui_players[playerTurn].setBalance(player[playerTurn].getAccountBalance());


            if (player[playerTurn].getPlayerPosition() == 3 ||player[playerTurn].getPlayerPosition() == 9 || player[playerTurn].getPlayerPosition() == 15 || player[playerTurn].getPlayerPosition() == 21){
                // Write something with chance-cards.
                gui.displayChanceCard(deck.toString());
            }

            if (player[playerTurn].getPlayerPosition() == 6){
                player[playerTurn].setPlayerPosition(18);

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
