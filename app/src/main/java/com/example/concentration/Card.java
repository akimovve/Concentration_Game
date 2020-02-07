package com.example.concentration;

class Card {
    boolean isFaceUp = false;
    boolean isMatched = false;
    int identifier;

    private static int identifierFactory = 0;

    Card() {
        this.identifier = getUniqueIdentifier();
    }

    private static int getUniqueIdentifier() {
        identifierFactory += 1;
        return identifierFactory;
    }
}
