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

    int noPlayer;
    int startMoney;
    int indexPlayerOwner;
    //Random player starts
    int playerTurn;

    Player[] player;

    //Starts game
    public void startGame() {
        System.out.println(gameBoard.getFields().length);
        // Creates a gameboard which allows the player to select language
        gameBoard.createGameBoard();
        gui_controller.createGameBoard(gameBoard.getFields());
        gui_controller.guiStart();
        Helper.lang = gui_controller.getUserButtonPressed("Choose language: ", "Dansk", "English");
        // Closes the gameboard and launches a new one with the chose language
        gui_controller.guiClose();
        gameBoard.createGameBoard();
        gui_controller.createGameBoard(gameBoard.getFields());
        gui_controller.guiStart();


        // Sets the number of player praticipating in the game
        noPlayer = Integer.parseInt(gui_controller.getUserButtonPressed("Choose number of players", "2", "3", "4"));
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
            player[i] = new Player(startMoney, 0, gui_controller.getUserString("Enter player name: "));
            gui_controller.getField(0);
            gui_controller.createGUI_Car(i, "startMoney", startMoney);
            gui_controller.createGUI_Player(i, player[i]);
        }

        /**
         * Main game loop.
         */
        while (true) {
            //Important note: the game stops until "OK" is pressed in the GUI
            if (player[playerTurn].getHasCard()){
                gui_controller.getShowMessage("You get to use the card you got earlier");
                manageField();
            }
            //Checks if the player is in jail and gives the option to pay to get out of jail
            if (player[playerTurn].getJail()) {
                String userinput;
                if (player[playerTurn].getJailCard()){
                    userinput = gui_controller.getUserButtonPressed(player[playerTurn].getName() + " you are in jail. Pay to get out, or use your card", "Pay 1M", "Use your card");
                }
                else{
                    userinput = gui_controller.getUserButtonPressed(player[playerTurn].getName() + " you are in jail. Pay to get out, or use your card", "Pay 1M");
                }
                if(userinput.equals("Pay 1M")) {
                    player[playerTurn].addAccountBalance(-1);
                }
                if(userinput.equals("Use your card")){
                    player[playerTurn].setJailCard(false);
                }
                gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                player[playerTurn].setJail(false);
            }

            // Rolls the dice
            gui_controller.getShowMessage(player[playerTurn].getName() + "'s turn. Press OK to roll");
            cup.rollDices();
            gui_controller.setDices(cup.getDice1(), cup.getDice2());

            //Loop that makes the players go around in a circle instead of breaking at field 24
            gui_controller.setGui_car(playerTurn, player[playerTurn].getPlayerPosition() + cup.getSum(), player[playerTurn].getPlayerPosition());
            player[playerTurn].addPlayerPosition(cup.getSum());

            // Sends player to jail
            if (player[playerTurn].getPlayerPosition() == 18) {
                gui_controller.getShowMessage("You landed on the 'Go to Jail' field and have been sent to prison.");
                player[playerTurn].setPlayerPosition(6);
                gui_controller.setGui_car(playerTurn, 6, 18);
                player[playerTurn].setJail(true);
            }

            // Gets the index of the owner of a field
            checkForOwner();

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
            // Win condition
            if (player[playerTurn].getAccountBalance() < 0) {
                winner2();
                break;
            }
            // Next players turn
            turn();


            for (int i = 0; i < noPlayer; i++) {
                System.out.print(player[i].getName() + " " + player[i].getAccountBalance() + " ");
            }
            System.out.print("\n");
        }

    }


    /**
     * Gets the index number of the owner for a field
     */
    private void checkForOwner() {
        // Handles null-pointer error in case noone owns the field.
        try {
            for (int i = 0; i < noPlayer - 1; i++) {
                // Finds the index of the owner of the current field and saves it
                if (gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName().equals(player[i].getName())) {
                    indexPlayerOwner = i;
                }
            }
        }
        // Handles the exception, sets index to the current players index to ensure nothing happens
        catch (NullPointerException e) {
            indexPlayerOwner = playerTurn;
        }
    }

    /**
     * Switches the players turn
     */
    private void turn() {
        // adds 1 to get the next players turn if the next player turns is within the amount of players
        if (playerTurn < noPlayer - 1){
            playerTurn += 1;
            // Sets the player turn to first player if player turn exceeds max amount of players
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
        // Finds empty fields
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
        // Checks if player is "at the edge" of the board, either 0 or 23 in this case. Edge cases is handled seperately.
        if (player[playerTurn].getPlayerPosition() != gameBoard.getFields().length - 1 && player[playerTurn].getPlayerPosition() != 0) {
            //Checks if the field in front of the field is owned by
            if (Objects.equals(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(player[playerTurn].getPlayerPosition() + 1).getOwnerName())) {
                // Makes player pay double rent to the field owner
                doubleRentPayment();
            }
            //Checks if the field before the field is owned by the same player
            else if (Objects.equals(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(player[playerTurn].getPlayerPosition() - 1).getOwnerName())){
                // Makes player pay double rent to the field owner
                doubleRentPayment();
            }
            else{
                // Makes the player pay normal rent
                rentPayment();
            }
        // Handles the special case where a player lands on field 23
        } else if (player[playerTurn].getPlayerPosition() == 23){
            if (Objects.equals(gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(player[playerTurn].getPlayerPosition() - 1).getOwnerName())){
                // Makes player pay double rent to the field owner
                doubleRentPayment();
            }
            else{
                // Makes the player pay normal rent
                rentPayment();
            }
        }

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
        String field = gui_controller.getUserButtonPressed("choose a field", "first field", "second field");
        if (field.equals("first field")){
            gui_controller.setGui_car(playerTurn, fieldOne, player[playerTurn].getPlayerPosition());
            player[playerTurn].setPlayerPosition(fieldOne);
            if (gui_controller.getField(fieldOne).getOwnerName() == null) {
                freeField();
            } else if(!Objects.equals(player[playerTurn].getName(), gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName())){
                System.out.println("IM ACCOUNTING 1");
                accounting();
            }
        } else {
            gui_controller.setGui_car(playerTurn, fieldTwo, player[playerTurn].getPlayerPosition());
            player[playerTurn].setPlayerPosition(fieldTwo);
            if (gui_controller.getField(fieldTwo).getOwnerName() == null) {
                freeField();
            } else if(!Objects.equals(player[playerTurn].getName(), gui_controller.getField(player[playerTurn].getPlayerPosition()).getOwnerName())){
                System.out.println("Im account2");
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
            int userInput = Integer.parseInt(gui_controller.getUserButtonPressed("CHOOSE A FIELD TO BUY", "1", "2", "4", "5", "7", "8", "10", "11", "13", "14", "16", "17", "19", "20", "22", "23"));
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

    private String winner(){
        Integer[] arr;
        String winner = "";
        if (noPlayer == 2){
            arr = new Integer[2];
            for (int i = 0; i < 2; i++) {
                arr[i] = player[playerTurn].getAccountBalance();
            }
        } else if(noPlayer == 3){
            arr = new Integer[3];
            for (int i = 0; i < 3; i++) {
                arr[i] = player[playerTurn].getAccountBalance();
            }
        } else{
            arr = new Integer[4];
            for (int i = 0; i < 4; i++) {
                arr[i] = player[playerTurn].getAccountBalance();
            }
        }
        Arrays.sort(arr, Collections.reverseOrder());
        if (Objects.equals(arr[0], arr[1])){
            if(valueOfAllProperties(0) > valueOfAllProperties(1)){
            }
        }else{
            for (int i = 0; i < noPlayer; i++) {
                if(player[i].getAccountBalance() == arr[0]){
                    winner = player[i].getName();

                }
            }
        }
        return winner;
    }

    private void winner2() {
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
                gui_controller.getShowMessage("Cash was equal. Winner is " + player[index[0]].getName() + " with " + playerEqualBalance[0] + "$$ in field-value");
            }else{
                System.out.println(Arrays.toString(playerEqualBalance) + "is index3");
                System.out.println("Winner is " + player[index[1]].getName() + " with " + playerEqualBalance[1] + "$$");
                gui_controller.getShowMessage("Cash was equal. Field value was equal. It's a draw between " + player[index[0]].getName() + " and " + player[index[1]].getName() + " with " + playerEqualBalance[1] + "$$ in field-value");
            }
        } else{

            System.out.println(player[index[0]].getName() + " is the winner with " + player[index[0]].getAccountBalance() + "$$");
            gui_controller.getShowMessage(player[index[0]].getName() + " is the winner with " + player[index[0]].getAccountBalance() + "$$ in cash");

        }

    }

    /**
     * Draws a card
     */
    private void drawCard(){
        deck.draw();
        gui_controller.displayChanceCard(deck.getLastCard().getCardDescription());
        gui_controller.getShowMessage("You have drawn a card");
        chanceCards(deck.getLastCard().getIndex());
        while(deck.getLastCard().getCardDescription() == null){
            chanceCards(deck.draw().getIndex());
        }
        gui_controller.getUserButtonPressed("Press when ready", "Continue");
        gui_controller.displayChanceCard(" ");
    }

    /**
     * Handles all chance card cases
     */
    private void chanceCards(int card) {
        gui_controller.displayChanceCard(deck.getLastCard().getCardDescription());
        switch (card) {
            case 0:
                if(noPlayer >= 4) {
                    if (playerTurn == 2) {
                        manageField();
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
                int userChoice = Integer.parseInt(gui_controller.getUserButtonPressed("DU MÅ RYKKE OP TIL 5 FELTER", "1", "2", "3", "4", "5"));
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
                String userChoice1 = gui_controller.getUserButtonPressed("DU MÅ RYKKE ET FELT ELLER TRÆKKE ET KORT TIL", "1", "TRÆK ET KORT TIL");
                if(userChoice1.equals("TRÆK ET KORT TIL")) {
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
                if(noPlayer >= 3) {
                    if (playerTurn == 3) {
                        manageField();
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
                String userChoice2 = gui_controller.getUserButtonPressed("ORANGE ELLER GRØN?", "ORANGE", "GRØN");
                if(userChoice2.equals("ORANGE")) {
                    moveToColor(15, 10, 11);
                    break;
                }
                if(userChoice2.equals("GRØN")){
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
                if (deck.getLastCard() == null)
                    break;
                else {
                    if (playerTurn == 0) {
                        manageField();

                    } else {
                        player[0].setHasCard(true);
                        drawCard();
                    }
                    break;
                }
            case 12:
                if (deck.getLastCard() == null)
                    break;
                else {
                    if (playerTurn == 1) {
                        manageField();
                    } else {
                        player[1].setHasCard(true);
                        drawCard();
                    }
                    break;

                }
            case 13:
                player[playerTurn].addAccountBalance(1);
                gui_controller.setGUI_AccountBalance(playerTurn, player[playerTurn].getAccountBalance());
                break;
            case 14:
                String userChoice3 = gui_controller.getUserButtonPressed("PINK ELLER MØRKEBLÅT?", "PINK", "MØRKEBLÅT");
                if(userChoice3.equals("PINK")) {
                    moveToColor(9, 7, 8);
                    break;
                }
                if(userChoice3.equals("MØRKEBLÅT")){
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
                String userChoice4 = gui_controller.getUserButtonPressed("LYSEBLÅT ELLER RØDT?", "LYSEBLÅT", "RØDT");
                if(userChoice4.equals("LYSEBLÅT")) {
                    moveToColor(9, 4, 5);
                    break;

                }
                if(userChoice4.equals("RØDT")){
                    moveToColor(15, 13,14);
                    break;
                }
            case 19:
                String userChoice5 = gui_controller.getUserButtonPressed("BRUNT ELLER GULT?", "BRUNT", "GULT");
                if(userChoice5.equals("BRUNT")) {
                    moveToColor(3, 1, 2);
                    break;

                }
                if(userChoice5.equals("GULT")){
                    moveToColor(21, 16,17);
                    break;
                }


        }
    }

}
