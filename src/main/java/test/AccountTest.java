package test;

import org.Game.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account = new Account();
    Integer knownBalance = 0;
    @Test
    void getPlayerBalance() {
    }

    @Test
    void setPlayerBalance() {
        account.setPlayerBalance(1000);
        knownBalance = 1000;
        assertEquals(account.getPlayerBalance(), 1000);

        for(int i = 0; i<5; i++){
            account.setPlayerBalance(1000);
            knownBalance += 1000;
            assertEquals(account.getPlayerBalance(), knownBalance);
        }
    }
}