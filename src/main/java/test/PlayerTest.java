package test;

import org.Game.Cup;
import org.Game.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
Cup cup = new Cup();
Player player = new Player(1000,0,"");
    @RepeatedTest(100)
    void addPlayerPosition() {

        for(int i = 0; i<=10; i++) {
            cup.rollDices();
            player.addPlayerPosition(cup.getSum());
            assertTrue(player.getPlayerPosition()<24 && player.getPlayerPosition()>=0);
        }
    }
}