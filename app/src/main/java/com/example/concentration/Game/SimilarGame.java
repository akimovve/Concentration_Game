package com.example.concentration.Game;

import com.example.concentration.Info.Literals;
import java.util.ArrayList;
import java.util.Collections;

class SimilarGame {

    ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Integer> openedCards = new ArrayList<>();
    private int indexOfOneAndOnlyFaceUpCard = -1;
    int mistakePoints = 0;

    SimilarGame(int numberOfPairsOfCards) {
        for (int i = 0; i < numberOfPairsOfCards * 2; i++) {
            Card card = new Card();
            cards.add(card);
        }

        for (int j = numberOfPairsOfCards * 2 - 1; j >= 0; j -= 2) {
            cards.get(j).identifier = cards.get(j-1).identifier;
        }
        Collections.shuffle(cards);
    }

    void chooseCard(int index) {
        if (!cards.get(index).isMatched) {
            int matchIndex = indexOfOneAndOnlyFaceUpCard;
            if (matchIndex != -1 && matchIndex != index) {
                if (cards.get(matchIndex).identifier == cards.get(index).identifier) {
                    cards.get(matchIndex).isMatched = true;
                    cards.get(index).isMatched = true;
                    mistakePoints += Literals.match;
                } else {
                    if (openedCards.contains(index)) {
                        mistakePoints -= Literals.miss;
                    }
                    if (openedCards.contains(matchIndex)) {
                        mistakePoints -= Literals.miss;
                    }
                    openedCards.add(index);
                    openedCards.add(matchIndex);
                }
                cards.get(index).isFaceUp = true;
                indexOfOneAndOnlyFaceUpCard = -1;
            } else {
                for (Card value : cards) value.isFaceUp = false;
                cards.get(index).isFaceUp = true;
                indexOfOneAndOnlyFaceUpCard = index;
            }
        }
    }

    boolean checkForAllMatchedCards() {
        boolean result = false;
        for (int i = 0; i < cards.size(); i++) {
            if (!cards.get(i).isMatched) {
                result = true;
                break;
            }
        }
        return !result;
    }
}
