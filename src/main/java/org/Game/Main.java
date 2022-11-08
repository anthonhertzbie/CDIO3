package org.Game;

import java.util.Arrays;

import static org.Game.Helper.lang;

public class Main {
    public static void main(String[] args) {
        lang = "Dansk";
        Deck deck = new Deck();
        System.out.println(deck);
    }
}