package org.Game;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class GameController {
    Cup cup = new Cup();
    GUI_Controller gui_controller = new GUI_Controller();
    Random random = new Random();
    GameBoard gameBoard = new GameBoard();
    Deck deck;

    Helper helper = new Helper();

    int noPlayer;
    int startMoney;
    int indexPlayerOwner;
    //Random player starts
    int playerTurn;

    Player[] player;

    //Starts game
    public void startGame() {

        // Creates a gameboard which allows the player to select language
        gameBoard.createGameBoard();
        gui_controller.createGameBoard(gameBoard.getFields());
        gui_controller.guiStart();
        Helper.lang = gui_controller.getUserButtonPressed("Choose language: ", "Dansk", "English");
        // Closes the gameboard and launches a new gameboard with the selected langauge
        gui_controller.guiClose();
        gameBoard.createGameBoard();
        gui_controller.createGameBoard(gameBoard.getFields());
        gui_controller.guiStart();



        noPlayer = Integer.parseInt(gui_controller.getUserButtonPressed(helper.lineReader("_Messages",0), "2", "3", "4"));
        gui_controller.setGUI_NumberOfPlayersAndCars(noPlayer);
        player = new Player[noPlayer];
        deck = new Deck(noPlayer);


        // shuffles the deck
        deck.shuffle();
        //Sets starting balance in accordance to number of players
        if (noPlayer == 2) {
            startMoney = 20;
        } else if (noPlayer == 3) {
            startMoney = 18;
        } else if (noPlayer == 4) {
            startMoney = 16;
        }
        // Chooses a random starting player
        playerTurn = random.nextInt(0, noPlayer);
        // creates the players and adds them to the GUI
        for (int i = 0; i < noPlayer; i++) {

            player[i] = new Player(startMoney, 0, gui_controller.getUserString(helper.lineReader("_Messages",1)));

            gui_controller.getField(0);
            gui_controller.createGUI_Car(i, "startMoney", startMoney);
            gui_controller.createGUI_Player(i, player[i]);
        }


        while (true) {
            //Important note: the game stops until "OK" is pressed in the GUI
            //Checks if the player is in jail and gives the option to pay to get out of jail
            if (player[playerTurn].getJail()) {
                String userinput;
                if (player[playerTurn].getJailCard()){
                    userinput = gui_controller.getUserButtonPressed(player[playerTurn].getName() + helper.lineReader("_Messages",2), helper.lineReader("_Messages",3), helper.lineReader("_Messages", 4));
                }
                else{
                    userinput = gui_controller.getUserButtonPressed(player[playerTurn].getName() + helper.lineReader("_Messages",38), helper.lineReader("_Messages",3));
                }
                if(userinput.equals(helper.lineReader("_Messages",3))) {
                    player[playerTurn].addAccountBalance(-1);
                }
                else if(userinput.equals(helper.lineReader("_Messages",4))){
                    player[playerTurn].setJailCard(false);
                }
                gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                player[playerTurn].setJail(false);
            }
            if (player[playerTurn].getHasCard()){
                gui_controller.getShowMessage(player[playerTurn].getName() + helper.lineReader("_Messages",5));
                if (playerTurn == 0){
                    gui_controller.displayChanceCard(deck.getCard(11).getCardDescription());
                } else if (playerTurn == 1){
                    gui_controller.displayChanceCard(deck.getCard(12).getCardDescription());
                } else if (playerTurn == 2){
                    gui_controller.displayChanceCard(deck.getCard(0).getCardDescription());
                } else if (playerTurn == 3){
                    gui_controller.displayChanceCard(deck.getCard(5).getCardDescription());
                }
                player[playerTurn].setHasCard(false);
                gui_controller.displayChanceCard("");
                manageField();
            }
            // Rolls the dice
            gui_controller.getShowMessage(player[playerTurn].getName() + helper.lineReader("_Messages",6));
            cup.rollDices();
            gui_controller.setDices(cup.getDice1(), cup.getDice2());
            sleep();

            //Loop that makes the players go around in a circle instead of breaking at field 24
            gui_controller.setGui_car(playerTurn, player[playerTurn].getPlayerPosition() + cup.getSum(), player[playerTurn].getPlayerPosition());
            player[playerTurn].addPlayerPosition(cup.getSum());
            sleep();


            // Sends player to jail
            if (player[playerTurn].getPlayerPosition() == 18) {
                gui_controller.getShowMessage(helper.lineReader("_Messages",7));
                player[playerTurn].setPlayerPosition(6);
                gui_controller.setGui_car(playerTurn, 6, 18);
                player[playerTurn].setJail(true);
            }

            //Checks if a field is not owned, and if the field is buyable (e.g. not start and chance-cards)
            if (gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()) != 0) {
                buyField();
            }
            // Checks if another player owns the field and pays them
            else if (!Objects.equals(player[playerTurn].getName(), gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName())) {
                accounting();
            }
            // Displays a chance card if landing on chance fields
            if (player[playerTurn].getPlayerPosition() == 3 || player[playerTurn].getPlayerPosition() == 9 || player[playerTurn].getPlayerPosition() == 15 || player[playerTurn].getPlayerPosition() == 21) {
                drawCard();
            }

            if (player[playerTurn].getAccountBalance() < 0) {
                winner();
                break;
            }



            turn();
            for (int i = 0; i < noPlayer; i++) {
                System.out.print(player[i].getName() + " " + player[i].getAccountBalance() + " ");
            }
            System.out.print("\n");
        }

    }
    //Taken from: https://www.javatpoint.com/thread-sleep-in-java
    private void sleep(){
        try{Thread.sleep(0);}catch(InterruptedException e){System.out.println(e);}
    }

    // Checks if current field is owned by someone, and returns their index number.
    // Handles gui null-point-error when a field is not yet owned by anyone
    private void checkForOwner() {
        try {
            for (int i = 0; i < noPlayer; i++) {
                // Finds the index of the owner of the current field
                if (gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals(player[i].getName())) {
                    indexPlayerOwner = i;
                    System.out.println(indexPlayerOwner);
                    System.out.println(playerTurn);
                }
            }
        }
        // Handles the exception. Proceeds with the code.
        catch (NullPointerException e) {
            indexPlayerOwner = playerTurn;
            //System.out.println("indexPlayerOwner is fxed");
        }
        System.out.println(helper.lineReader("_Messages",8) + indexPlayerOwner);
    }
    /**
     * Increments the playerTurn variable with one.
     */
    private void turn() {
        if (playerTurn < noPlayer - 1){
            playerTurn += 1;
        } else {
            playerTurn = 0;
        }

    }

    /**
     * Counts all available fields
     * @return number of available fields
     */
    private int ownableFields(){
        int count = 0;
        for (int i = 0; i < 24; i++) {
            if(gui_controller.getField(i).getOwnerName() == null && Integer.parseInt(gui_controller.getField(i).getRent()) != 0){
                count++;
            }
        }
        return count;
    }

    /**
     * Gives the field to the current player for free!
     */
    private void freeField(){
        // Sets player as new owner
        gui_controller.getField(player[playerTurn].getPlayerPosition()).setOwnerName(player[playerTurn].getName());
        //Colours the field the same colour as the car to show who owns the field
        gui_controller.getField(player[playerTurn].getPlayerPosition()).setBorder(gui_controller.gui_Player[playerTurn].getPrimaryColor());
    }

    /**
     * Handles the rent payment, checks for ownership of neighbouring fields.
     */
    private void accounting(){
        checkForOwner();

        if (player[playerTurn].getPlayerPosition() != 23 && player[playerTurn].getPlayerPosition() != 0) {
            if (Objects.equals(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(player[playerTurn].getPlayerPosition() + 1).getOwnerName())) {
                //Sets the owners account balance after collecting double rent
                doubleRentPayment();
            }
            else if (Objects.equals(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(player[playerTurn].getPlayerPosition() - 1).getOwnerName())){
                doubleRentPayment();
            }
            // Makes the player pay normal rent
            else{
                rentPayment();
            }
        } else if(player[playerTurn].getPlayerPosition() == 23){
            if (Objects.equals(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(player[playerTurn].getPlayerPosition() - 1).getOwnerName())){
                doubleRentPayment();
            }
            // Makes the player pay normal rent
            else{
                rentPayment();
            }
        }
        // Same as prev but backwards

    }

    /**
     * Manages the payment when the owner of the current field also owns the neighbouring
     */
    private void doubleRentPayment() {
        player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()) * 2);
        player[indexPlayerOwner].addAccountBalance(Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()) * -2);
        gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
        gui_controller.setGUI_AccountBalance(indexPlayerOwner, player[indexPlayerOwner].getAccountBalance());
    }
    private void rentPayment(){
        player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()));
        //The owner collects rent
        player[indexPlayerOwner].addAccountBalance(Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()) * -1);
        gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
        gui_controller.setGUI_AccountBalance(indexPlayerOwner, player[indexPlayerOwner].getAccountBalance());
    }

    /**
     * Handles the purchase of a field.
     */
    private void buyField(){
        // Sets player as new owner
        gui_controller.getField(player[playerTurn].getPlayerPosition()).setOwnerName(player[playerTurn].getName());
        //Colours the field the same colour as the car to show who owns the field
        gui_controller.getField(player[playerTurn].getPlayerPosition()).setBorder(gui_controller.gui_Player[playerTurn].getPrimaryColor());
        //Sets the new account balance after buying the property
        player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()));
        gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
    }

    /**
     * Moves you to the color of one of the two fields
     * @param chanceCardField The next chance field, in relation to the color.
     * @param fieldOne the first field of that particular color.
     * @param fieldTwo the subsequent field.
     */
    private void moveToColor(int chanceCardField, int fieldOne, int fieldTwo){
        if (player[playerTurn].getPlayerPosition() >= chanceCardField) {
            player[playerTurn].addAccountBalance(2);
        }
        String field = gui_controller.getUserButtonPressed(helper.lineReader("_Messages",9), helper.lineReader("_Messages",10), helper.lineReader("_Messages",11));
        if (field.equals(helper.lineReader("_Messages",10))){
            gui_controller.setGui_car(playerTurn, fieldOne, player[playerTurn].getPlayerPosition());
            player[playerTurn].setPlayerPosition(fieldOne);
            if (gui_controller.getField(fieldOne).getOwnerName() == null) {
                freeField();
            } else if(!Objects.equals(player[playerTurn].getName(), gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName())){
                accounting();
            }
        } else {
            gui_controller.setGui_car(playerTurn, fieldTwo, player[playerTurn].getPlayerPosition());
            player[playerTurn].setPlayerPosition(fieldTwo);
            if (gui_controller.getField(fieldTwo).getOwnerName() == null) {
                freeField();
            } else if(!Objects.equals(player[playerTurn].getName(), gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName())){
                accounting();
            }
        }
        gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
    }

    /**
     *Manages the player-specific chance cards
     */
    private void manageField(){
        if (ownableFields() == 0) {
            int userInput = Integer.parseInt(gui_controller.getUserButtonPressed(helper.lineReader("_Messages",12), "1", "2", "4", "5", "7", "8", "10", "11", "13", "14", "16", "17", "19", "20", "22", "23"));
            if (userInput < player[playerTurn].getPlayerPosition()){
                player[playerTurn].addAccountBalance(2);
            }
            gui_controller.setGui_car(playerTurn, userInput, player[playerTurn].getPlayerPosition());
            player[playerTurn].setPlayerPosition(userInput);
            checkForOwner();
            buyField();
            player[indexPlayerOwner].addAccountBalance(Integer.parseInt(gui_controller.getField(userInput).getRent()) * -1);
            gui_controller.setGUI_AccountBalance(indexPlayerOwner, player[indexPlayerOwner].getAccountBalance());
            gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
        } else {
            while (gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName() != null || Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()) == 0) {
                gui_controller.setGui_car(playerTurn, 1 + player[playerTurn].getPlayerPosition(), player[playerTurn].getPlayerPosition());
                player[playerTurn].addPlayerPosition(1);
            }
            buyField();
        }
    }

    private int valueOfAllProperties(int player){
        int netWorth = 0;
        for (int i = 0; i < 24; i++) {
            if(Objects.equals(this.player[player].getName(), gui_controller.getField(i).getOwnerName())){
                netWorth += Integer.parseInt(gui_controller.getField(i).getRent());
            }
        }
        return netWorth;
    }

    private void winner() {
        Integer[] playerBalances = new Integer[noPlayer];
        Integer[] playerEqualBalance;
        int playerno = 0;
        int[] index = new int[noPlayer];
        String winner;

        for (int i = 0; i < noPlayer; i++){
            playerBalances[i] = player[i].getAccountBalance() * 10 + i;
            System.out.println(Arrays.toString(playerBalances));
        }
        Arrays.sort(playerBalances, Collections.reverseOrder());
        for (int i = 0; i < noPlayer; i++){
            index[i] = playerBalances[i] % 10;
            System.out.println("first index: " + index[i]);
        }

        // If players have the same amount of end cash...
        if (playerBalances[0] - playerBalances[0] % 10 == playerBalances[1] - playerBalances[1] % 10) {
            System.out.println("Im running this now");
            for(int i = 0; i < noPlayer; i++){
                if(playerBalances[0] - playerBalances[0] % 10 == playerBalances[i] - playerBalances[i] % 10){
                    playerno += 1;
                }
            }
            System.out.println(playerno + "playerno is//playerCount is : " + noPlayer);
            playerEqualBalance = new Integer[playerno];
            for (int i = 0; i < playerno; i++) {
                playerEqualBalance[i] = index[i];
                for (int ii = 0; ii < gameBoard.getFields().length; ii++) {
                    if (Objects.equals(gui_controller.getField(ii).getOwnerName(), player[index[i]].getName())){
                        try{
                            playerEqualBalance[i] += Math.abs(Integer.parseInt(gui_controller.getField(ii).getRent())*10);
                        } catch(NullPointerException e){
                            playerEqualBalance[i] += 0;
                        }
                    }
                }
            }
            System.out.println(Arrays.toString(playerEqualBalance) + " is playerownedvalue");
            System.out.println(Arrays.toString(index) + " is index");
            Arrays.sort(playerEqualBalance, Collections.reverseOrder());
            for (int i = 0; i < playerno; i++){
                index[i] = playerEqualBalance[i] % 10;
                playerEqualBalance[i] = playerEqualBalance[i] - playerEqualBalance[i] % 10;
            }
            System.out.println("playerBalnces : " + Arrays.toString(playerEqualBalance));
            System.out.println("Player to highest index has the balance:" + player[index[0]].getAccountBalance());
            System.out.println("Player to highest index has the balance:" + player[index[1]].getAccountBalance());
            System.out.println("Player to highest index has the balance:" + player[index[2]].getAccountBalance());
            if (!Objects.equals(playerEqualBalance[0], playerEqualBalance[1])) {
                System.out.println(Arrays.toString(playerEqualBalance) + "is index1");
                System.out.println("Winner is " + player[index[0]].getName() + " with " + playerEqualBalance[0] + "$$");
                gui_controller.getShowMessage(helper.lineReader("_Messages",13) + player[index[0]].getName() + helper.lineReader("_Messages",14) + playerEqualBalance[0] + helper.lineReader("_Messages",15));
            }else{
                System.out.println(Arrays.toString(playerEqualBalance) + "is index3");
                System.out.println("Winner is " + player[index[1]].getName() + " with " + playerEqualBalance[1] + "$$");
                gui_controller.getShowMessage(helper.lineReader("_Messages",16) + player[index[0]].getName() + helper.lineReader("_Messages",17) + player[index[1]].getName() + helper.lineReader("_Messages",14) + playerEqualBalance[1] + helper.lineReader("_Messages",15));
            }
        } else{

            System.out.println(player[index[0]].getName() + " is the winner with " + player[index[0]].getAccountBalance() + "$$");
            gui_controller.getShowMessage(player[index[0]].getName() + helper.lineReader("_Messages",18) + player[index[0]].getAccountBalance() + helper.lineReader("_Messages",19));

        }

    }

    /**
     * Draws a card
     */
    private void drawCard(){
        deck.draw();
        while(deck.getLastCard().getCardDescription() == null){
            deck.draw();
        }
        gui_controller.getShowMessage(helper.lineReader("_Messages",20));
        gui_controller.displayChanceCard(deck.getLastCard().getCardDescription());
        chanceCards(deck.getLastCard().getIndex());
        gui_controller.getUserButtonPressed(helper.lineReader("_Messages",21), helper.lineReader("_Messages",22));
        gui_controller.displayChanceCard(" ");
    }

    /**
     * Handles all chance card cases
     */
    private void chanceCards(int card) {
        gui_controller.displayChanceCard(deck.getLastCard().getCardDescription());
        switch (card) {
            case 0:
                if(noPlayer >= 3) {
                    if (playerTurn == 2) {
                        player[2].setHasCard(true);
                    } else {
                        player[2].setHasCard(true);
                        drawCard();
                    }
                } else{
                    drawCard();
                }
                break;


            case 1:
                gui_controller.setGui_car(playerTurn, 0, player[playerTurn].getPlayerPosition());
                player[playerTurn].setPlayerPosition(0);
                player[playerTurn].addAccountBalance(2);
                gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                break;
            case 2:
                int userChoice = Integer.parseInt(gui_controller.getUserButtonPressed(helper.lineReader("_Messages",23), "1", "2", "3", "4", "5"));
                gui_controller.setGui_car(playerTurn, player[playerTurn].getPlayerPosition() + userChoice, player[playerTurn].getPlayerPosition());
                player[playerTurn].addPlayerPosition(userChoice);
                checkForOwner();
                if(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gui_controller.getField(player[playerTurn].getPlayerPosition()).getRent()) != 0){
                    buyField();
                } else if(!player[indexPlayerOwner].getName().equals(player[playerTurn].getName())){
                    accounting();
                }
                break;
            case 3:
                moveToColor(15,10,11);
                break;
            case 4:
                String userChoice1 = gui_controller.getUserButtonPressed(helper.lineReader("_Messages",24), "1", helper.lineReader("_Messages",25));
                if(userChoice1.equals(helper.lineReader("_Messages",25))) {
                    drawCard();
                    break;
                }
                if(userChoice1.equals("1")){
                    gui_controller.setGui_car(playerTurn, 1 + player[playerTurn].getPlayerPosition(), player[playerTurn].getPlayerPosition());
                    player[playerTurn].addPlayerPosition(1);
                    if(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName() == null){
                        buyField();
                    } else if(!player[playerTurn].getName().equals(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName())){
                        accounting();
                    } else{
                        accounting();
                    }
                    break;
                }
            case 5:
                if(noPlayer >= 4) {
                    if (playerTurn == 3) {
                        player[3].setHasCard(true);
                    } else {
                        player[3].setHasCard(true);
                        drawCard();
                    }
                } else{
                    drawCard();
                }
                break;
            case 6:
                player[playerTurn].addAccountBalance(-2);
                gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                break;
            case 7:
                String userChoice2 = gui_controller.getUserButtonPressed(helper.lineReader("_Messages",26), helper.lineReader("_Messages",27), helper.lineReader("_Messages",28));
                if(userChoice2.equals(helper.lineReader("_Messages",27))) {
                    moveToColor(15, 10, 11);
                    break;
                }
                if(userChoice2.equals(helper.lineReader("_Messages",28))){
                    moveToColor(21,19,20);
                    break;
                }
            case 8:
                moveToColor(9, 4, 5);
                break;
            case 9:
                player[playerTurn].setJailCard(true);
                break;
            case 10:
                if (gui_controller.getField(23).getOwnerName() == null) {
                    gui_controller.setGui_car(playerTurn, 23, player[playerTurn].getPlayerPosition());
                    player[playerTurn].setPlayerPosition(23);
                    buyField();
                    break;
                } else {
                    gui_controller.setGui_car(playerTurn, 23, player[playerTurn].getPlayerPosition());
                    player[playerTurn].setPlayerPosition(23);
                    accounting();
                    break;
                }
            case 11:
                if (playerTurn == 0) {
                    player[0].setHasCard(true);
                } else {
                    player[0].setHasCard(true);
                    drawCard();
                }
                break;
            case 12:
                if (playerTurn == 1) {
                    player[1].setHasCard(true);
                } else {
                    player[1].setHasCard(true);
                    drawCard();
                }
                break;
            case 13:
                player[playerTurn].addAccountBalance(1);
                gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                break;
            case 14:
                String userChoice3 = gui_controller.getUserButtonPressed(helper.lineReader("_Messages",29), helper.lineReader("_Messages",30), helper.lineReader("_Messages",31));
                if(userChoice3.equals(helper.lineReader("_Messages",30))) {
                    moveToColor(9, 7, 8);
                    break;
                }
                if(userChoice3.equals(helper.lineReader("_Messages",31))){
                    moveToColor(22, 22, 23);
                    break;
                }
            case 15:
                player[playerTurn].addAccountBalance(2);
                gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                break;
            case 16:
                moveToColor(15, 13, 14);
                break;
            case 17:
                if (player[playerTurn].getPlayerPosition() > 14) {
                    player[playerTurn].addAccountBalance(2);
                    gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                }
                if (gui_controller.getField(10).getOwnerName() == null) {
                    gui_controller.setGui_car(playerTurn, 10, player[playerTurn].getPlayerPosition());
                    player[playerTurn].setPlayerPosition(10);
                    freeField();
                } else {
                    gui_controller.setGui_car(playerTurn, 10, player[playerTurn].getPlayerPosition());
                    player[playerTurn].setPlayerPosition(10);
                    accounting();
                }
                break;
            case 18:
                String userChoice4 = gui_controller.getUserButtonPressed(helper.lineReader("_Messages",32), helper.lineReader("_Messages",33), helper.lineReader("_Messages",34));
                if(userChoice4.equals(helper.lineReader("_Messages",33))) {
                    moveToColor(9, 4, 5);
                    break;

                }
                if(userChoice4.equals(helper.lineReader("_Messages",34))){
                    moveToColor(15, 13,14);
                    break;
                }
            case 19:
                String userChoice5 = gui_controller.getUserButtonPressed(helper.lineReader("_Messages",35), helper.lineReader("_Messages",36), helper.lineReader("_Messages",37));
                if(userChoice5.equals(helper.lineReader("_Messages",36))) {
                    moveToColor(3, 1, 2);
                    break;

                }
                if(userChoice5.equals(helper.lineReader("_Messages",37))){
                    moveToColor(21, 16,17);
                    break;
                }


        }
    }

}
