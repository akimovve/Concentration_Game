package com.example.concentration;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.Info.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Game extends AppCompatActivity {

    int numberOfCards;
    int connect = 0;
    int howShorter = 0;
    int id = -1;
    final int convertIdToIndex = R.id.button_00;
    String[] emojiTypes = {"🏰","🐨","🐝","🦂","🦖","⛄️","🛸","💻","🏁","💂","💍","🐒","🐊","🎄","🏍","👾","🦁","🐿","🔥","🌘","🍕","⚽️","🥁","🧀","🛩","📸","🎁","🍏","🐩","🐓","🍁",
            "🌈","🦈","🛏","📚","🗿","🎭","🍿","🥥","🍆","🦔","🎮️","🌶","🐘","🚔","🎡","🏔","🚄","🎬","🐙","🍄","🌵","🐢","👑","🧞","👻","🧤","🎓","🎪","🐶","🐲","🍓","🏆","🎰" };

    public Game(){}

    ArrayList<Button> buttons = new ArrayList<>();
    Constants constants = new Constants();
    Concentration gameLogic;
    Button pauseButton;
    TextView levelNumTextView, flipsCountView;



    public void setClick(final Boolean fl, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int index = 0; index < numberOfCards; index++)
                    buttons.get(index).setClickable(fl);
            }
        }, delay);
    }

    public void appearanceOfCards() {
        for (int index = 0; index < numberOfCards; index++) {
            final Button button = buttons.get(index);
            button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setVisibility(View.VISIBLE);
                }
            }, constants.delayForFirstAppearance - 200);
            constants.delayForFirstAppearance += constants.delayBetweenAppearance;
        }
    }

    public void openCardsRandomly() {
        @SuppressLint("UseSparseArrays")
        Map<Integer,Boolean> checkTheRepeat = new HashMap<>();
        for (int index = 0; index < numberOfCards; index++) {
            checkTheRepeat.put(index, false); // all buttons haven't opened yet => false
        }

        ArrayList<Integer> randArrOfFirstIndexes = new ArrayList<>(); // array of random sequence of cards' indexes
        for (int index = 0; index < numberOfCards; index++)
            randArrOfFirstIndexes.add(index);

        Collections.shuffle(randArrOfFirstIndexes);

        if (numberOfCards == 4) {
            int deleteRandom = (int)(Math.random() * 4);
            randArrOfFirstIndexes.remove(deleteRandom);
        } else {
            int[] secondRandArray = new int[(int) (Math.random() * (numberOfCards / 3) + (numberOfCards / 3))]; // random size [(numberOfCards / 4);(numberOfCards/2)]
            for (int index = 0; index < secondRandArray.length; index++) {
                int randomIndexOfFirstArray;
                do {
                    randomIndexOfFirstArray = (int) (Math.random() * numberOfCards);
                } while (checkTheRepeat.get(randomIndexOfFirstArray));
                secondRandArray[index] = randArrOfFirstIndexes.get(randomIndexOfFirstArray);
                checkTheRepeat.put(randomIndexOfFirstArray, true);
            }

            for (int value : secondRandArray)
                randArrOfFirstIndexes.add(value);

            Collections.shuffle(randArrOfFirstIndexes);

            int index = 0;
            do {
                if (randArrOfFirstIndexes.get(index).equals(randArrOfFirstIndexes.get(index + 1))) {
                    int random;
                    do {
                        random = (int) (Math.random() * randArrOfFirstIndexes.size());
                    } while (random == index || random == index + 1);
                    Integer temp = randArrOfFirstIndexes.get(index + 1);
                    randArrOfFirstIndexes.add(index + 1, randArrOfFirstIndexes.get(random));
                    randArrOfFirstIndexes.add(random, temp);
                }
                index++;
            } while (index < randArrOfFirstIndexes.size() - 1);
        }
        outPutRandomly(randArrOfFirstIndexes);
    }

    public void outPutRandomly(ArrayList<Integer> array) {
        for (int index = 0; index < array.size(); index++) {
            final int randomButtonIndex = array.get(index);
            final Button finalBut = pressedButton(randomButtonIndex);
            finalBut.setTextSize(43);
            finalBut.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalBut.setText(getEmoji(gameLogic.cards.get(randomButtonIndex))); // opened
                    finalBut.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                }
            }, constants.delayForFirstAppearance - connect); // default. DO NOT TOUCH!
            constants.delayForFirstAppearance += constants.timeCardIsClose; // time the card is being opened
            finalBut.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalBut.setText(""); // closed
                    finalBut.getBackground().setColorFilter(getResources().getColor(R.color.buttonsColor), PorterDuff.Mode.MULTIPLY);
                }
            }, constants.delayForFirstAppearance); // default. DO NOT TOUCH!
            constants.delayForFirstAppearance += constants.timeCardIsOpen; // time between closed and next opened card
        }
    }

    public Button pressedButton(int index) {
        return buttons.get(index);
    }

    @SuppressLint("ResourceType")
    public int getIndex(int index) {
        return index-convertIdToIndex;
    }

    public void updateViewFromModel() {
        for (int index = 0; index < numberOfCards; index++) {
            Card card = gameLogic.cards.get(index);
            functionForPressedButton(buttons.get(index), card);
        }
    }

    public void functionForPressedButton(Button button, Card card) {
        button.setTextSize(43);
        if (card.isFaceUp) {
            button.setText(getEmoji(card));
            button.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        } else {
            button.setText("");
            if (!card.isMatched) {
                button.getBackground().setColorFilter(getResources().getColor(R.color.buttonsColor), PorterDuff.Mode.MULTIPLY);
            } else {
                button.getBackground().setColorFilter(getResources().getColor(R.color.noColor), PorterDuff.Mode.MULTIPLY);
                button.setEnabled(false);
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    Map<Integer, String> emoji = new HashMap<>();

    public String getEmoji(Card card) {
        if (emoji.get(card.identifier) == null && emojiTypes.length > 0) {
            int randomIndex = (int)(Math.random()*(emojiTypes.length - howShorter));
            emoji.put(card.identifier, emojiTypes[randomIndex]);
            if (emojiTypes.length - 1 - randomIndex >= 0)
                System.arraycopy(emojiTypes, randomIndex + 1, emojiTypes, randomIndex, emojiTypes.length - 1 - randomIndex);
            howShorter++;
        }

        if (emoji.get(card.identifier) != null)
            return emoji.get(card.identifier);
        else return "?";
    }
}
