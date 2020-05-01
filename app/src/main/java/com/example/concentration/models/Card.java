package com.example.concentration.models;

public class Card {

    public boolean isFaceUp = false, isMatched = false;
    public int identifier;
    private static int idCreator = 0;

    public Card() {
        this.identifier = getUniqueIdentifier();
    }

    private static int getUniqueIdentifier() {
        idCreator++;
        return idCreator;
    }
}
