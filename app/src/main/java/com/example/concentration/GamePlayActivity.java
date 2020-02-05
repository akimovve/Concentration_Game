package com.example.concentration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.concentration.Info.Constants;
import com.example.concentration.Levels.LevelUpActivity;
import com.example.concentration.Menu.StartActivity;
import com.example.concentration.SettingsMenu.SettingsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GamePlayActivity extends AppCompatActivity {

    final int numberOfCards = 12;
    int flipCount = 0;
    int maxLevel = 5;

    Constants constants = new Constants();
    int connect = 0;
    boolean flag = true;


    Concentration game = new Concentration((numberOfCards + 1) / 2);
    private TextView flipsCountView, levelTextView;
    private Button button01, button02, button03, button04, button05, button06, button07, button08, button09, button10, button11, button12, button13, button14, button15, button16;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            connect = bundle.getInt("whichLevel");
            flag = bundle.getBoolean("levelUp");
        }


        flipsCountView = findViewById(R.id.flipsCountView);
        levelTextView  = findViewById(R.id.levelTextView);
        Button settingsButton = findViewById(R.id.settingsButton);
        button01 = findViewById(R.id.button01);
        button02 = findViewById(R.id.button02);
        button03 = findViewById(R.id.button03);
        button04 = findViewById(R.id.button04);
        button05 = findViewById(R.id.button05);
        button06 = findViewById(R.id.button06);
        button07 = findViewById(R.id.button07);
        button08 = findViewById(R.id.button08);
        button09 = findViewById(R.id.button09);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        /*button13 = findViewById(R.id.button13);
        button14 = findViewById(R.id.button14);
        button15 = findViewById(R.id.button15);
        button16 = findViewById(R.id.button16);

         */
        final int levelNumber = Constants.getLevelNumber(flag);
        levelTextView.setText("Level " + levelNumber);




        setClick(false,1); // time for becoming cards not clickable
        appearanceOfCards(); // cards start to appear one by one
        openCardsRandomly(); // cards start opening randomly
        setClick(true, constants.delayForFirstAppearance + connect); // delay of start of the game

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamePlayActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        OnClickListener onButtonsClick = new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                flipCount += 1;
                flipsCountView.setText("Flips: " + flipCount);
                game.chooseCard(getIndex(v.getId()));
                updateViewFromModel();

                if (!game.checkForAllMatchedCards()) {
                    if (levelNumber < maxLevel) {
                        Intent intent = new Intent(GamePlayActivity.this, LevelUpActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                    } else {
                        Intent intent = new Intent(GamePlayActivity.this, StartActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };

        button01.setOnClickListener(onButtonsClick);
        button02.setOnClickListener(onButtonsClick);
        button03.setOnClickListener(onButtonsClick);
        button04.setOnClickListener(onButtonsClick);
        button05.setOnClickListener(onButtonsClick);
        button06.setOnClickListener(onButtonsClick);
        button07.setOnClickListener(onButtonsClick);
        button08.setOnClickListener(onButtonsClick);
        button09.setOnClickListener(onButtonsClick);
        button10.setOnClickListener(onButtonsClick);
        button11.setOnClickListener(onButtonsClick);
        button12.setOnClickListener(onButtonsClick);
        /*button13.setOnClickListener(onButtonsClick);
        button14.setOnClickListener(onButtonsClick);
        button15.setOnClickListener(onButtonsClick);
        button16.setOnClickListener(onButtonsClick);
        */
    }

    public void appearanceOfCards() {
        for (int i = 0; i < numberOfCards; i++) {
            final Button but = pressedButton(i);
            but.postDelayed(new Runnable() {
                @Override
                public void run() {
                    but.setVisibility(View.VISIBLE);
                }
            }, constants.delayForFirstAppearance);
            constants.delayForFirstAppearance += constants.delayBetweenAppearance;
        }
    }

    public void openCardsRandomly() {
        @SuppressLint("UseSparseArrays")
        Map<Integer, Boolean> checkTheRepeat = new HashMap<>();
        for (int k = 0; k < numberOfCards; k++) {
            checkTheRepeat.put(k, false);
        }

        ArrayList<Integer> randArrOfFirstIndexes = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            randArrOfFirstIndexes.add(i);
        }
        Collections.shuffle(randArrOfFirstIndexes);

        int[] secondRandArray = new int[(int) (Math.random() * (numberOfCards / 3) + (numberOfCards / 3))]; // random size [(numberOfCards / 4);(numberOfCards/2)]
        for (int i = 0; i < secondRandArray.length; i++) {
            int randomIndexOfFirstArray;
            do {
                randomIndexOfFirstArray = (int) (Math.random() * numberOfCards);

            } while (checkTheRepeat.get(randomIndexOfFirstArray));
            secondRandArray[i] = randArrOfFirstIndexes.get(randomIndexOfFirstArray);
            checkTheRepeat.put(randomIndexOfFirstArray,true);
        }

        for (int value : secondRandArray) {
            randArrOfFirstIndexes.add(value);
        }
        Collections.shuffle(randArrOfFirstIndexes);
        outPutRandomly(randArrOfFirstIndexes);
    }

    public void outPutRandomly(ArrayList<Integer> array) {
        for (int rIndex = 0; rIndex < array.size(); rIndex++) {
            final int randomButtonIndex = array.get(rIndex);
            final Button finalBut = pressedButton(randomButtonIndex);
            finalBut.setTextSize(60);
            finalBut.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalBut.setText(emoji(game.cards.get(randomButtonIndex)));
                    finalBut.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                }
            }, constants.delayForFirstAppearance + 100); // default. DO NOT TOUCH!
            constants.delayForFirstAppearance += constants.timeCardIsOpen - connect; // time the card is being opened
            finalBut.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalBut.setText("");
                    finalBut.getBackground().setColorFilter(getResources().getColor(R.color.buttonsColor), PorterDuff.Mode.MULTIPLY);
                }
            }, constants.delayForFirstAppearance + 100); // default. DO NOT TOUCH!
            constants.delayForFirstAppearance += constants.timeCardIsClose - connect; // time between closed and next opened card
        }
    }

    public void setClick(final Boolean fl, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button01.setClickable(fl);
                button02.setClickable(fl);
                button03.setClickable(fl);
                button04.setClickable(fl);
                button05.setClickable(fl);
                button06.setClickable(fl);
                button07.setClickable(fl);
                button08.setClickable(fl);
                button09.setClickable(fl);
                button10.setClickable(fl);
                button11.setClickable(fl);
                button12.setClickable(fl);
                /*button13.setClickable(fl);
                button14.setClickable(fl);
                button15.setClickable(fl);
                button16.setClickable(fl);

                 */
            }
        },delay);
    }

    public void updateViewFromModel() {
        for (int i = 0; i < numberOfCards; i++) {
            Card card = game.cards.get(i);
            functionForPressedButton(pressedButton(i), card);
        }
    }

    public void functionForPressedButton(Button button, Card card) {
        button.setTextSize(60);
        if (card.isFaceUp) {
            button.setText(emoji(card));
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

    String[] emojiChoices = {"🤡","👾","🦁","🐿","🔥","🌘","🍕","⚽️","🥁","🚕","🛩","📸","🎁","🍏","🍄","🌵","🐢","👑","🧞","👻","🧤","🎓","🐵","🐶","🐲","🍓","🧁","🏆","🎰"};


    @SuppressLint("UseSparseArrays")
    Map<Integer, String> emoji = new HashMap<>();

    int howShorter = 0;

    public String emoji(Card card) {
        if (emoji.get(card.identifier) == null && emojiChoices.length > 0) {
            int randomIndex = (int)(Math.random()*(emojiChoices.length - howShorter));
            emoji.put(card.identifier, emojiChoices[randomIndex]);
            if (emojiChoices.length - 1 - randomIndex >= 0)
                System.arraycopy(emojiChoices, randomIndex + 1, emojiChoices, randomIndex, emojiChoices.length - 1 - randomIndex);
            howShorter++;
        }

        if (emoji.get(card.identifier) != null) {
            return emoji.get(card.identifier);
        } else {
            return "?";
        }
    }

    public Button pressedButton(int index) {
        Button chosenButton = button01;
        switch (index) {
            case 0: { chosenButton = button01; break; }
            case 1: { chosenButton = button02; break; }
            case 2: { chosenButton = button03; break; }
            case 3: { chosenButton = button04; break; }
            case 4: { chosenButton = button05; break; }
            case 5: { chosenButton = button06; break; }
            case 6: { chosenButton = button07; break; }
            case 7: { chosenButton = button08; break; }
            case 8: { chosenButton = button09; break; }
            case 9: { chosenButton = button10; break; }
            case 10: { chosenButton = button11; break; }
            case 11: { chosenButton = button12; break; }
            /*case 12: { chosenButton = button13; break; }
            case 13: { chosenButton = button14; break; }
            case 14: { chosenButton = button15; break; }
            case 15: { chosenButton = button16; break; }

             */

        }
        return chosenButton;
    }

    @SuppressLint("ResourceType")
    public int getIndex(int index) {
        return index-2131296290;
    }
}
