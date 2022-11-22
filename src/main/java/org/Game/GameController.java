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

    Player[] Player;

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



        noPlayer = Integer.parseInt(gui_controller.getUserButtonPressed("Choose number of players", "2", "3", "4"));
        gui_controller.setGUI_NumberOfPlayersAndCars(noPlayer);
        Player = new Player[noPlayer];
        deck = new Deck(noPlayer);


        // shuffles the deck
        deck.shuffle();
        //Sets starting balance in accordance to number of players
        if (noPlayer == 2) {
            startMoney = 20;
        } else if (noPlayer == 3) {
            startMoney = 6;
        } else if (noPlayer == 4) {
            startMoney = 16;
        }
        // Chooses a random starting player
        playerTurn = random.nextInt(0, noPlayer);
        // creates the players and adds them to the GUI
        for (int i = 0; i < noPlayer; i++) {

            Player[i] = new Player(startMoney, 0, gui_controller.getUserString("Enter player name: "));

            gui_controller.getField(0);
            gui_controller.createGUI_Car(i, "startMoney", startMoney);
            gui_controller.createGUI_Player(i, Player[i]);
        }


        while (true) {
            System.out.println("[Before turn] True balance of " + Player[playerTurn].getName() + " is " + Player[playerTurn].getAccountBalance());
            //Important note: the game stops until "OK" is pressed in the GUI

            //Checks if the player is in jail and gives the option to pay to get out of jail
            if (Player[playerTurn].getJail()) {
                gui_controller.getUserButtonPressed(Player[playerTurn].getName() + " you are in jail. Pay to get out:", "Pay 1M");
                Player[playerTurn].addAccountBalance(-1);
                gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
                Player[playerTurn].setJail(false);
                gui_controller.getShowMessage("Thanks for the money man! Press OK to roll the dices: ");
                cup.rollDices();
                gui_controller.setDices(cup.getDice1(), cup.getDice2());
            } else {
                // Rolls the dices normally if the player is not in jail
                gui_controller.getShowMessage(Player[playerTurn].getName() + "'s turn. Press OK to roll");
                cup.rollDices();
                gui_controller.setDices(cup.getDice1(), cup.getDice2());
            }

            //Loop that makes the players go around in a circle instead of breaking at field 24
            gui_controller.setGui_car(playerTurn, Player[playerTurn].getPlayerPosition() + cup.getSum(), Player[playerTurn].getPlayerPosition());
            Player[playerTurn].addPlayerPosition(cup.getSum());


            // Sends player to jail
            if (Player[playerTurn].getPlayerPosition() == 18) {
                gui_controller.getShowMessage("You landed on the 'Go to Jail' field and have been sent to prison.");
                Player[playerTurn].setPlayerPosition(6);
                gui_controller.setGui_car(playerTurn, 6, 18);
                Player[playerTurn].setJail(true);
            }
            // Checks if current field is owned by someone, and returns their index number.
            // Handles gui null-point-error when a field is not yet owned by anyone
            try {
                for (int i = 0; i < noPlayer - 1; i++) {
                    // Finds the index of the owner of the current field
                    if (gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName().equals(Player[i].getName())) {
                        indexPlayerOwner = i;
                    }
                }
            }
            // Handles the exception. Proceeds with the code.
            catch (NullPointerException e) {
                indexPlayerOwner = playerTurn;
                System.out.println("indexPlayerOwner is fxed");
            }

            //Checks if a field is not owned, and if the field is buyable (e.g. not start and chance-cards)
            if (gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()) != 0) {
                buyField();
            }
            // Checks if another player owns the field and pays them
            else if (!Objects.equals(Player[playerTurn].getName(), gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName())) {
                System.out.println("Accounting");
                accounting();
            }
            // Displays a chance card if landing on chance fields
            if (Player[playerTurn].getPlayerPosition() == 3 || Player[playerTurn].getPlayerPosition() == 9 || Player[playerTurn].getPlayerPosition() == 15 || Player[playerTurn].getPlayerPosition() == 21) {
                //drawCard();
            }
            System.out.println(Player[playerTurn].getAccountBalance() + "Account balance is");
            System.out.println("[after turn] True balance of " + Player[playerTurn].getName() + " is " + Player[playerTurn].getAccountBalance());
            if (Player[playerTurn].getAccountBalance() < 0) {
                winner2();
                break;
            }

            turn();
        }
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
            if(gui_controller.getField(i).getOwnerName() == null){
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
        gui_controller.getField(Player[playerTurn].getPlayerPosition()).setOwnerName(Player[playerTurn].getName());
        //Colours the field the same colour as the car to show who owns the field
        gui_controller.getField(Player[playerTurn].getPlayerPosition()).setBorder(gui_controller.gui_Player[playerTurn].getPrimaryColor());
    }

    /**
     * Handles the rent payment, checks for ownership of neighbouring fields.
     */
    private void accounting(){
        System.out.println(Player[playerTurn].getPlayerPosition());
        if (Player[playerTurn].getPlayerPosition() != 23 && Objects.equals(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(Player[playerTurn].getPlayerPosition() + 1).getOwnerName())){
            //Sets the owners account balance after collecting double rent
            Player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()) * 2);
            Player[indexPlayerOwner].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()) * -2);
            gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
            gui_controller.setGUI_AccountBalance(indexPlayerOwner, Player[indexPlayerOwner].getAccountBalance());
        }
        // Same as prev but backwards
        else if (Player[playerTurn].getPlayerPosition() != 0 && Objects.equals(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName(), gui_controller.getField(Player[playerTurn].getPlayerPosition() - 1).getOwnerName())){
            Player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()) * 2);
            Player[indexPlayerOwner].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()) * -2);
            gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
            gui_controller.setGUI_AccountBalance(indexPlayerOwner, Player[indexPlayerOwner].getAccountBalance());
        }
        // Makes the player pay normal rent
        else{
            System.out.println("Is this where i break?");
            Player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()));
            //The owner collects rent
            Player[indexPlayerOwner].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()) * -1);
            gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
            gui_controller.setGUI_AccountBalance(indexPlayerOwner, Player[indexPlayerOwner].getAccountBalance());
            System.out.println(Player[playerTurn].getAccountBalance() + " p2: " + Player[indexPlayerOwner].getAccountBalance());
        }
    }

    /**
     * Handles the purchase of a field.
     */
    private void buyField(){
        // Sets player as new owner
        gui_controller.getField(Player[playerTurn].getPlayerPosition()).setOwnerName(Player[playerTurn].getName());
        //Colours the field the same colour as the car to show who owns the field
        gui_controller.getField(Player[playerTurn].getPlayerPosition()).setBorder(gui_controller.gui_Player[playerTurn].getPrimaryColor());
        //Sets the new account balance after buying the property
        Player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()));
        gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
    }

    /**
     * Moves you to the color of one of the two fields
     * @param chanceCardField The next chance field, in relation to the color.
     * @param fieldOne the first field of that particular color.
     * @param fieldTwo the subsequent field.
     */
    private void moveToColor(int chanceCardField, int fieldOne, int fieldTwo){
        if (Player[playerTurn].getPlayerPosition() >= chanceCardField) {
            Player[playerTurn].addAccountBalance(2);
            gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
        }
        String field = gui_controller.getUserButtonPressed("choose a field", "first field", "second field");
        if (field.equals("first field")){
            gui_controller.setGui_car(playerTurn, fieldOne, Player[playerTurn].getPlayerPosition());
            Player[playerTurn].setPlayerPosition(fieldOne);
            if (gui_controller.getField(fieldOne).getOwnerName() == null) {
                freeField();
            } else if(Objects.equals(Player[playerTurn].getName(), gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName())){
            } else{
                accounting();
            }
        } else {
            gui_controller.setGui_car(playerTurn, fieldTwo, Player[playerTurn].getPlayerPosition());
            Player[playerTurn].setPlayerPosition(fieldTwo);
            if (gui_controller.getField(fieldTwo).getOwnerName() == null) {
                freeField();
            } else if(Objects.equals(Player[playerTurn].getName(), gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName())){
            } else{
                buyField();
            }
        }
    }

    /**
     *Manages the player-specific chance cards
     */
    private void manageField(){
        if (ownableFields() == 0) {
            int userInput = Integer.parseInt(gui_controller.getUserButtonPressed("CHOOSE A FIELD TO BUY", "1", "2", "4", "5", "7", "8", "10", "11", "13", "14", "16", "17", "19", "20", "22", "23"));
            gui_controller.setGui_car(playerTurn, userInput - 1, Player[playerTurn].getPlayerPosition());
            Player[playerTurn].setPlayerPosition(userInput - 1);
            Player[playerTurn].addAccountBalance(Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()));
            gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
        } else {
            while (gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName() == null && Integer.parseInt(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent()) != 0) {
                gui_controller.setGui_car(playerTurn, 1 + Player[playerTurn].getPlayerPosition() - 24, Player[playerTurn].getPlayerPosition());
                Player[playerTurn].addPlayerPosition(1);
            }
            buyField();
        }
    }

    private int valueOfAllProperties(int player){
        int netWorth = 0;
        for (int i = 0; i < 24; i++) {
            if(Objects.equals(Player[player].getName(), gui_controller.getField(i).getOwnerName())){
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
                arr[i] = Player[playerTurn].getAccountBalance();
            }
        } else if(noPlayer == 3){
            arr = new Integer[3];
            for (int i = 0; i < 3; i++) {
                arr[i] = Player[playerTurn].getAccountBalance();
            }
        } else{
            arr = new Integer[4];
            for (int i = 0; i < 4; i++) {
                arr[i] = Player[playerTurn].getAccountBalance();
            }
        }
        Arrays.sort(arr, Collections.reverseOrder());
        if (Objects.equals(arr[0], arr[1])){
            if(valueOfAllProperties(0) > valueOfAllProperties(1)){
            }
        }else{
            for (int i = 0; i < noPlayer; i++) {
                if(Player[i].getAccountBalance() == arr[0]){
                    winner = Player[i].getName();

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
            playerBalances[i] = Player[i].getAccountBalance() * 10 + i;
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
                    if (Objects.equals(gui_controller.getField(ii).getOwnerName(), Player[index[i]].getName())){
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
            System.out.println("Player to highest index has the balance:" + Player[index[0]].getAccountBalance());
            System.out.println("Player to highest index has the balance:" + Player[index[1]].getAccountBalance());
            System.out.println("Player to highest index has the balance:" + Player[index[2]].getAccountBalance());
            if (!Objects.equals(playerEqualBalance[0], playerEqualBalance[1])) {
                System.out.println(Arrays.toString(playerEqualBalance) + "is index1");
                System.out.println("Winner is " + Player[index[0]].getName() + " with " + playerEqualBalance[0] + "$$");
                gui_controller.getShowMessage("Cash was equal. Winner is " + Player[index[0]].getName() + " with " + playerEqualBalance[0] + "$$ in field-value");
            }else{
                System.out.println(Arrays.toString(playerEqualBalance) + "is index3");
                System.out.println("Winner is " + Player[index[1]].getName() + " with " + playerEqualBalance[1] + "$$");
                gui_controller.getShowMessage("Cash was equal. Field value was equal. It's a draw between " + Player[index[0]].getName() + " and " + Player[index[1]].getName() + " with " + playerEqualBalance[1] + "$$ in field-value");
            }
        } else{

            System.out.println(Player[index[0]].getName() + " is the winner with " + Player[index[0]].getAccountBalance() + "$$");
            gui_controller.getShowMessage(Player[index[0]].getName() + " is the winner with " + Player[index[0]].getAccountBalance() + "$$ in cash");

        }

    }

    /**
     * Draws a card
     */
    private void drawCard(){
        chanceCards(deck.draw().getIndex());
        while(deck.getLastCard().getCardDescription() == null){
            chanceCards(deck.draw().getIndex());
        }
        gui_controller.displayChanceCard(deck.getLastCard().getCardDescription());
        gui_controller.getUserButtonPressed(" ", "Continue");
    }

    /**
     * Handles all chance card cases
     */
    private void chanceCards(int card) {
        switch (card) {
            case 0:
                if (playerTurn == 2) {
                        manageField();

                } else {
                        Player[2].setHasCard(true);
                }
                break;


            case 1:
                gui_controller.setGui_car(playerTurn, 0, Player[playerTurn].getPlayerPosition());
                Player[playerTurn].setPlayerPosition(0);
                Player[playerTurn].addAccountBalance(2);
                gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
                break;
            case 2:
                int userChoice = Integer.parseInt(gui_controller.getUserButtonPressed("DU MÅ RYKKE OP TIL 5 FELTER", "1", "2", "3", "4", "5"));
                gui_controller.setGui_car(playerTurn, Player[playerTurn].getPlayerPosition() + userChoice, Player[playerTurn].getPlayerPosition());
                Player[playerTurn].addPlayerPosition(userChoice);
                if(gui_controller.getField(Player[playerTurn].getPlayerPosition()).getRent() == null && !Objects.equals(Player[playerTurn].getName() , gui_controller.getField(Player[playerTurn].getPlayerPosition()).getOwnerName())){
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
                    gui_controller.setGui_car(playerTurn, 1 + Player[playerTurn].getPlayerPosition(), Player[playerTurn].getPlayerPosition());
                    Player[playerTurn].addPlayerPosition(Player[playerTurn].getPlayerPosition() + 1);
                    break;
                }
            case 5:
                if (playerTurn == 3) {
                       manageField();

                } else {
                        Player[3].setHasCard(true);
                }
                break;
            case 6:
                Player[playerTurn].addAccountBalance(-2);
                gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
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
                Player[playerTurn].setJailCard(true);
                break;
            case 10:
                if (gui_controller.getField(23).getOwnerName() == null) {
                    gui_controller.setGui_car(playerTurn, 23, Player[playerTurn].getPlayerPosition());
                    Player[playerTurn].setPlayerPosition(23);
                    buyField();
                    break;
                } else {
                    gui_controller.setGui_car(playerTurn, 23, Player[playerTurn].getPlayerPosition());
                    Player[playerTurn].setPlayerPosition(23);
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
                        Player[0].setHasCard(true);
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
                        Player[1].setHasCard(true);
                    }
                    break;

                }
            case 13:
                Player[playerTurn].addAccountBalance(1);
                gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
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
                Player[playerTurn].addAccountBalance(2);
                gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
                break;
            case 16:
                moveToColor(15, 13, 14);
                break;
            case 17:
                if (Player[playerTurn].getPlayerPosition() > 14) {
                    Player[playerTurn].addAccountBalance(2);
                    gui_controller.setGUI_AccountBalance(playerTurn, Player[playerTurn].getAccountBalance());
                }
                if (gui_controller.getField(10).getOwnerName() == null) {
                    gui_controller.setGui_car(playerTurn, 10, Player[playerTurn].getPlayerPosition());
                    Player[playerTurn].setPlayerPosition(10);
                    freeField();
                } else {
                    gui_controller.setGui_car(playerTurn, 10, Player[playerTurn].getPlayerPosition());
                    Player[playerTurn].setPlayerPosition(10);
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
                    moveToColor(15, 14,13);
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
