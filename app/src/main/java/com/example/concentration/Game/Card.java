package com.example.concentration.Game;

class Card {

    boolean isFaceUp = false;
    boolean isMatched = false;
    int identifier;
    private static int idCreator = 0;

    Card() {
        this.identifier = getUniqueIdentifier();
    }

    private static int getUniqueIdentifier() {
        idCreator++;
        return idCreator;
    }
}
