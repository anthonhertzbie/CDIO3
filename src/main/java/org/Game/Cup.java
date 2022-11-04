package org.Game;

import java.util.Random;

import static org.Game.Dice.sides;

public class Cup {

    Dice dice1 = new Dice();
    Dice dice2 = new Dice();

    public Cup(){
    }

    private final Random random = new Random();



public void rollDices() {
    dice1.setFace(random.nextInt(1, sides + 1));
    dice2.setFace(random.nextInt(1, sides + 1));
}
public int getDice1(){
    return dice1.getFace();
}

public int getDice2(){
    return dice2.getFace();
}
public int getSum(){
    int sum = getDice2() + getDice1();
    return sum;
}

}
