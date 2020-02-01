package com.example.concentration;

class Concentration {

    Card[] cards;

    private int indexOfOneAndOnlyFaceUpCard = -1;

    Concentration(int numberOfPairsOfCards) {
        cards = new Card[numberOfPairsOfCards * 2];
        for (int i = 0; i < numberOfPairsOfCards * 2; i++) {
            Card card = new Card();
            cards[i] = card;
        }
        cards[1].identifier = cards[0].identifier;
        cards[3].identifier = cards[2].identifier;
    }

    void chooseCard(int index) {
        if (!cards[index].isMatched) {
            int matchIndex = indexOfOneAndOnlyFaceUpCard;
            if (matchIndex != -1 && matchIndex != index) {
                if (cards[matchIndex].identifier == cards[index].identifier) {
                    cards[matchIndex].isMatched = true;
                    cards[index].isMatched = true;
                }
                cards[index].isFaceUp = true;
                indexOfOneAndOnlyFaceUpCard = -1;
            } else {
                for (Card value : cards) value.isFaceUp = false;
                cards[index].isFaceUp = true;
                indexOfOneAndOnlyFaceUpCard = index;
            }
        }
    }
}
