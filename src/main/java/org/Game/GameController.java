package org.Game;

import java.util.Objects;
import java.util.Random;

public class GameController {

    GUI_Controller gui_controller = new GUI_Controller();
    Random random = new Random();
    Cup cup = new Cup();
    GameBoard gameBoard = new GameBoard();

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

    public int ownableFields(){
        int count = 0;
        for (int i = 0; i < 24; i++) {
            if(gameBoard.getField(i).getOwnerName() == null){
                count++;
            }
        }
        return count;
    }

    public void accounting(){
        if (player[playerTurn].getPlayerPosition() != 23 && Objects.equals(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gameBoard.getField(player[playerTurn].getPlayerPosition() + 1).getOwnerName())){
            //Sets the owners account balance after collecting double rent
            player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * 2);
            player[indexPlayerOwner].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * -2);
        }
        // Same as prev but backwards
        else if (player[playerTurn].getPlayerPosition() != 0 && Objects.equals(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gameBoard.getField(player[playerTurn].getPlayerPosition() - 1).getOwnerName())){
            player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * 2);
            player[indexPlayerOwner].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * -2);
        }
        // Makes the player pay normal rent
        else{
            player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
            //The owner collects rent
            player[indexPlayerOwner].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) * -1);
        }
    }
    public void buyField(){
        // Sets player as new owner
        gameBoard.getField(player[playerTurn].getPlayerPosition()).setOwnerName(player[playerTurn].getName());
        //Colours the field the same colour as the car to show who owns the field
        gameBoard.getField(player[playerTurn].getPlayerPosition()).setBorder(player[playerTurn].gui_Player.getPrimaryColor());
        //Sets the new account balance after buying the property
        player[playerTurn].setAccountBalance(Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()));
    }

    //Starts game
    public void startGame(){

        //Sets the ownerName as "For sale" on all the fields

        gui_controller.guiHelper("Dansk", gameBoard.createGameBoard());
        noPlayer = Integer.parseInt(gui_controller.getUserButtonPressed("Choose number of players", "2", "3", "4"));
        player = new Player[noPlayer];
        Deck deck = new Deck(noPlayer);


        // shuffles the deck
        deck.shuffle();
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
        // Chooses a random starting player
        playerTurn = random.nextInt(0, noPlayer);
        // creates the players and adds them to the GUI
        for (int i = 0; i < noPlayer; i++) {

            player[i] = new Player(i, startMoney, 0, gui_controller.getUserString("Enter player name: "), gameBoard.getField(0));
            gui_controller.addPlayer(player[i].getGui_Player());
        }


        while (true) {
            //Important note: the game stops until "OK" is pressed in the GUI


            // Rolls the dices and handles the jail function as diceroll is not allowed if in jail.
            if (player[playerTurn].getJail()){
                // ******** Lav et if-statement som trigger en ekstra knap hvis chancekort er trukket, mangler chancekort info **********\\\\\\\\
                gui_controller.getUserButtonPressed(player[playerTurn].getName() + " you are in jail. Pay to get out:", "Pay 1M");
                player[playerTurn].setAccountBalance(-1);
                player[playerTurn].setJail(false);
                gui_controller.getShowMessage("Thanks for the money man! Press OK to roll the dices: ");
                cup.rollDices();
                gui_controller.setDices(cup.getDice1(), cup.getDice2());
            }
            else {
                // Rolls the dices normally if the player is not in jail
                gui_controller.getShowMessage(player[playerTurn].getName() + "'s turn. Press OK to roll");
                cup.rollDices();
                gui_controller.setDices(cup.getDice1(),cup.getDice2());
            }

            //Loop that makes the players go around in a circle instead of breaking at field 24
            // IT BREAKS IF THE PLAYER LANDS ON FIELD 23 !!!!!!!!
            if (player[playerTurn].getPlayerPosition()+cup.getSum() < 24){
                player[playerTurn].addPlayerPosition(cup.getSum(),gameBoard.getField(cup.getSum() + player[playerTurn].getPlayerPosition()), gameBoard.getField(player[playerTurn].getPlayerPosition()));
            } else {
                player[playerTurn].addPlayerPosition(cup.getSum()-24, gameBoard.getField(cup.getSum() + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                player[playerTurn].setAccountBalance(2);
            }
            // Sends player to jail
            if (player[playerTurn].getPlayerPosition() == 18){
                gui_controller.getShowMessage("You landed on the 'Go to Jail' field and have been sent to prison.");
                player[playerTurn].setPlayerPosition(6, gameBoard.getField(6), gameBoard.getField(18));
                player[playerTurn].setJail(true);
            }
            // Handles gui nullpointererror when a field is not yet owned by anyone
            try {
                for (int i = 0; i < noPlayer - 1; i++) {
                    // Finds the index of the owner of the current field
                    if (gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals(player[i].getName())) {
                        indexPlayerOwner = i;
                    }
                }
            }
            // Handles the exception. Proceeds with the code.
            catch (NullPointerException e){
            }

            // checks if a field is not owned, and if the field is buyable (e.g. not start and chancecards)
            if (gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) != 0) {
                buyField();
            }
            // Checks if the current player own the field
            else if (!Objects.equals(player[playerTurn].getName(), gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName())){
                // checks if the field one step forward is also owned by the same color
                accounting();
            }
            // Displays a chance card if landing on chance fields
            if (player[playerTurn].getPlayerPosition() == 3 ||player[playerTurn].getPlayerPosition() == 9 || player[playerTurn].getPlayerPosition() == 15 || player[playerTurn].getPlayerPosition() == 21) {
                // Write something with chance-cards.
                gui_controller.displayChanceCard(deck.getFirstCard().getCardDescription());
                switch (deck.draw().getIndex()) {
                    case 0:
                        if(deck.getLastCard() == null)
                        break;
                        else{
                            if(playerTurn == 2){
                                if(ownableFields() == 0){
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() != null) {
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    accounting();
                                }
                                else{
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) != 0){
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    buyField();
                                }

                            } else {
                                player[2].setHasCard(true);
                            }
                            break;

                        }
                    case 1:
                        player[playerTurn].setPlayerPosition(0,gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                        player[playerTurn].setAccountBalance(player[playerTurn].getAccountBalance() - 1);
                    case 2:
                        int userChoice = Integer.parseInt(gui_controller.getUserButtonPressed("DU MÅ RYKKE OP TIL 5 FELTER", "1", "2", "3", "4", "5"));
                        player[playerTurn].addPlayerPosition(player[playerTurn].getPlayerPosition() + userChoice,gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                    case 3:
                        if(player[playerTurn].getPlayerPosition() > 11){
                            player[playerTurn].setAccountBalance(player[playerTurn].getAccountBalance() + 2);
                        }
                        if(gameBoard.getField(10).getOwnerName() == null){
                        player[playerTurn].setPlayerPosition(0,gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                        }
                    case 5:
                        if(deck.getLastCard() == null)
                            break;
                        else{
                            if(playerTurn == 3){
                                if(ownableFields() == 0){
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() != null) {
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    accounting();
                                }
                                else{
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) != 0){
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    buyField();
                                }
                            } else {
                                player[3].setHasCard(true);
                            }
                            break;

                        }
                    case 11:
                        if(deck.getLastCard() == null)
                            break;
                        else{
                            if(playerTurn == 0){
                                if(ownableFields() == 0){
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() != null) {
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    accounting();
                                }
                                else{
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) != 0){
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    buyField();
                                }

                            } else {
                                player[0].setHasCard(true);
                            }
                            break;

                        }
                    case 12:
                        if(deck.getLastCard() == null)
                            break;
                        else{
                            if(playerTurn == 1){
                                if(ownableFields() == 0){
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() != null) {
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    accounting();
                                }
                                else{
                                    while(gameBoard.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gameBoard.getField(player[playerTurn].getPlayerPosition()).getRent()) != 0){
                                        player[playerTurn].addPlayerPosition(1, gameBoard.getField(1 + player[playerTurn].getPlayerPosition() - 24), gameBoard.getField(player[playerTurn].getPlayerPosition()));
                                    }
                                    buyField();
                                }

                            } else {
                                player[1].setHasCard(true);
                            }
                            break;

                        }


                }
            }




            if(player[playerTurn].getAccountBalance() < 0){
                if(noPlayer == 2){

                    break;
                }
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
