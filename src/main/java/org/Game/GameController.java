package org.Game;

import gui_main.GUI;

import java.util.Objects;
import java.util.Random;

public class GameController {


    Random random = new Random();
    Cup cup = new Cup();
    GameBoard gameBoard = new GameBoard();
    Deck deck = new Deck();

    int noPlayer;
    int startMoney;
    int indexPlayerOwner;
    //Random player starts
    int playerTurn;

    //Instantiates Player class depending on number of players

    Player[] player;
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
        GUI gui = new GUI(gameBoard.createGameBoard());
        noPlayer = Integer.parseInt(gui.getUserSelection("Choose number of players", "2", "3", "4"));
        player = new Player[noPlayer];



        //Sets starting balance in accordance to number of players
        if (noPlayer == 2){
            startMoney = 20;
        }
        else if (noPlayer == 3){
            startMoney = 18;
        }
        else if (noPlayer == 4){
            startMoney = 16;
        }
        playerTurn = random.nextInt(0, noPlayer);
        for (int i = 0; i < noPlayer; i++) {
            player[i] = new Player(i, startMoney, 0, gui.getUserString("Enter player name: "), gameBoard.getField(0));
            gui.addPlayer(player[i].getGui_Player());
        }


        while (true) {
            //Important note: the game stops until "OK" is pressed in the GUI
            gui.showMessage(player[playerTurn].getName() + "'s turn. Press OK to roll");
            cup.rollDices();
            gui.setDice(cup.getDice1(), cup.getDice2());


            //Loop that makes the players go around in a circle instead of breaking at field 24
            // IT BREAKS IF THE PLAYER LANDS ON FIELD 23 !!!!!!!!
            if (player[playerTurn].getPlayerPosition()+cup.getSum() < 24){
                player[playerTurn].addPlayerPosition(cup.getSum(),gameBoard.getField(cup.getSum() + player[playerTurn].getPlayerPosition()), gameBoard.getField(player[playerTurn].getPlayerPosition()));
            } else {
                player[playerTurn].addPlayerPosition(cup.getSum()-24, gameBoard.getField(cup.getSum() + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                player[playerTurn].setAccountBalance(2);

                //May need to move this bit
                if (player[playerTurn].getPlayerPosition() == 18){
                    player[playerTurn].setAccountBalance(-2);
                }
            }




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
            System.out.println(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName());
            try {
            //if (ownerName for field is "For sale" and it is not field 0, 3, 6, 9, 12, 15, 18, 21), then...
            if (gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null &&
                    player[playerTurn].getPlayerPosition() != 0 && player[playerTurn].getPlayerPosition() != 3 &&
                    player[playerTurn].getPlayerPosition() != 6 && player[playerTurn].getPlayerPosition() != 9 &&
                    player[playerTurn].getPlayerPosition() != 12 && player[playerTurn].getPlayerPosition() != 15 &&
                    player[playerTurn].getPlayerPosition() != 18 && player[playerTurn].getPlayerPosition() != 21){
                System.out.println("I'm being run now");
                //Sets ownerName as current players name
                gameBoard.getField(player[playerTurn].getPlayerPosition()).setOwnerName(player[playerTurn].getName());
                gameBoard.getField(player[playerTurn].getPlayerPosition()).setBackGroundColor(player[playerTurn].gui_Player.getPrimaryColor());

                //Colours the field the same colour as the car to show who owns the field

                //Sets the new account balance after buying the property
                player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
            }

                //if (ownerName (current field) = ownerName (current field +1 or -1) and ownerName (current field) is not current player name), then...
            else if (gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals(
                        (gameBoard.getField(player[playerTurn].getPlayerPosition() + 1).getOwnerName())) ||
                        gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals((gameBoard.getField(player[playerTurn].getPlayerPosition() - 1).getOwnerName())) && (
                                !Objects.equals(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), player[playerTurn].getName()))) {

                    //Sets current player´s account balance after paying double rent
                    player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * 2);
                    //Sets the owners account balance after collecting double rent
                    player[indexPlayerOwner].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * -2);
                }
                //if (ownerName (current field) is not current player name), then...
                else if (!Objects.equals(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), player[playerTurn].getName())) {
                    //Current player pays rent
                    player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
                    //The owner collects rent
                    player[indexPlayerOwner].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * -1);
                }
            }
            catch (NullPointerException e){

            }



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
