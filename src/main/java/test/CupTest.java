package test;

import org.Game.Cup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CupTest {
    Cup cup = new Cup();
    Integer[] dices = {2,3,4,5,6,7,8,9,10,11,12};
    @RepeatedTest(1000)
    void rollDices() {
        cup.rollDices();
        assertTrue(Arrays.asList(dices).contains(cup.getSum()));
    }

    @Test
    void getDice1() {
    }

    @Test
    void getDice2() {
    }

    @Test
    void getSum() {
    }
}
