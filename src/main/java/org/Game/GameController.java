package org.Game;

import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class GameController {


    Random random = new Random();
    Cup cup = new Cup();
    GameBoard gameBoard = new GameBoard();
    Deck deck = new Deck();
    boolean jail = false;


    int noPlayer = 2;
    int startMoney = 0;
    int indexPlayerOwner;


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

        //Sets the ownerName as "For sale" on all the fields
        GUI gui = new GUI(gameBoard.gameBoard());
        for (int i = 0; i < 24; i++) {
            gameBoard.getField(i).setOwnerName("For sale");
        }
        //Sets starting balance in accordance to number of players
        if (noPlayer == 2){
            startMoney = 20;
        }
        if (noPlayer == 3){
            startMoney = 18;
        }
        if (noPlayer == 4){
            startMoney = 16;
        }
        for (int i = 0; i < noPlayer; i++) {
            player[i] = new Player(startMoney, 0, gui.getUserString("Enter player name: "));
            cars[i] = new GUI_Car();

            //Makes the gui-player and determines the players attributes
            gui_players[i] = new GUI_Player(player[i].getName(), startMoney, cars[i]);
            gui.addPlayer(gui_players[i]);

            //Makes the firs car red, the second car black and so on
            if(i == 0){
                cars[i].setPrimaryColor(Color.RED);
            } else if(i == 1){
                cars[i].setPrimaryColor(Color.black);
            } else if(i == 2){
                cars[i].setPrimaryColor(Color.green);
            } else if(i == 3){
                cars[i].setPrimaryColor(Color.blue);
            }

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


            //Loop that makes the players go around in a circle instead of breaking at field 24
            // IT BREAKS IF THE PLAYER LANDS ON FIELD 23 !!!!!!!!
            if (player[playerTurn].getPlayerPosition()+cup.getSum() < 24){
                player[playerTurn].addPlayerPosition(cup.getSum());
            } else {
                player[playerTurn].addPlayerPosition(cup.getSum()-24);
                player[playerTurn].setAccountBalance(2);
                gui_players[playerTurn].setBalance(player[playerTurn].getAccountBalance());

                //May need to move this bit
                if (player[playerTurn].getPlayerPosition() == 18){
                    player[playerTurn].setAccountBalance(-2);
                }
            }

            //Just places the car in gui at the new position
            gameBoard.getField(player[playerTurn].getPlayerPosition()).setCar(gui_players[playerTurn], true);


            try {
                for (int i = 0; i < noPlayer - 1; i++) {
                    if (gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals(player[i].getName())) {
                        indexPlayerOwner = i;
                    }
                }
            }
            // ?
            catch (Exception e){

            }
            System.out.println(player[playerTurn].getPlayerPosition());

            System.out.println(gameBoard.getField(5).getOwnerName());

            //if (ownerName for field is "For sale" and it is not field 0, 3, 6, 9, 12, 15, 18, 21), then...
            if (gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals("For sale") &&
                    player[playerTurn].getPlayerPosition() != 0 && player[playerTurn].getPlayerPosition() != 3 &&
                    player[playerTurn].getPlayerPosition() != 6 && player[playerTurn].getPlayerPosition() != 9 &&
                    player[playerTurn].getPlayerPosition() != 12 && player[playerTurn].getPlayerPosition() != 15 &&
                    player[playerTurn].getPlayerPosition() != 18 && player[playerTurn].getPlayerPosition() != 21){

                //Sets ownerName as current players name
                gameBoard.getField(player[playerTurn].getPlayerPosition()).setOwnerName(player[playerTurn].getName());
                System.out.println(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName());
                System.out.println(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals("Ejes af: "));

                //Colours the field the same colour as the car to show who owns the field
                gameBoard.getField(player[playerTurn].getPlayerPosition()).setBackGroundColor(gui_players[playerTurn].getPrimaryColor());

                //Sets the new account balance after buying the property
                player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
            }
            //if (ownerName (current field) = ownerName (current field +1 or -1) and ownerName (current field) is not current player name), then...
            if (gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals(
                    (gameBoard.getField(player[playerTurn].getPlayerPosition()+1).getOwnerName()))||
                    gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals(
                            (gameBoard.getField(player[playerTurn].getPlayerPosition()-1).getOwnerName()))&&(
                            !Objects.equals(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), player[playerTurn].getName()))){

                //Sets current player´s account balance after paying double rent
                player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent())*2);
                //Sets the owners account balance after collecting double rent
                player[indexPlayerOwner].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent())*-2);
            }
            //if (ownerName (current field) is not current player name), then...
            if (!Objects.equals(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), player[playerTurn].getName())){
                //Current player pays rent
                player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
                //The owner collects rent
                player[indexPlayerOwner].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent())*-1);
            }


            //Updates the gui-balance
            gui_players[playerTurn].setBalance(player[playerTurn].getAccountBalance());


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
