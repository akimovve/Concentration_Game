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

import com.example.concentration.Menu.Menu;
import com.example.concentration.SettingsMenu.Settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    int numberOfCards = 12;
    int delay = 3500;
    Concentration game = new Concentration((numberOfCards + 1) / 2);
    private TextView flipsCountView;
    private Button button01, button02, button03, button04, button05, button06, button07, button08, button09, button10, button11, button12;
    private Button settingsButton;
    int flipCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipsCountView = findViewById(R.id.flipsCountView);
        settingsButton = findViewById(R.id.settingsButton);
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

        appearanceOfCards();
        for (int i = 0; i < numberOfCards; i++) {
            openRandomCard(delay);
            delay += 1000;
        }


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        OnClickListener onButtonsClick = new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (game.checkForAllMatchedCards()) {
                    flipCount += 1;
                    flipsCountView.setText("Flips: " + flipCount);
                    int cardNumber = 0;
                    switch (v.getId()) {
                        case R.id.button01: { cardNumber = 0; break; }
                        case R.id.button02: { cardNumber = 1; break; }
                        case R.id.button03: { cardNumber = 2; break; }
                        case R.id.button04: { cardNumber = 3; break; }
                        case R.id.button05: { cardNumber = 4; break; }
                        case R.id.button06: { cardNumber = 5; break; }
                        case R.id.button07: { cardNumber = 6; break; }
                        case R.id.button08: { cardNumber = 7; break; }
                        case R.id.button09: { cardNumber = 8; break; }
                        case R.id.button10: { cardNumber = 9; break; }
                        case R.id.button11: { cardNumber = 10; break; }
                        case R.id.button12: { cardNumber = 11; break; }
                    }
                    game.chooseCard(cardNumber);
                    updateViewFromModel();
                } else {
                    Intent intent = new Intent(MainActivity.this, Menu.class);
                    startActivity(intent);
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

    }

    public void appearanceOfCards() {
        Button but = button01;
        int secDelay = 500;
        for (int i = 0; i < numberOfCards; i++) {
            switch (i) {
                case 0: { but = button01; break; }
                case 1: { but = button02; break; }
                case 2: { but = button03; break; }
                case 3: { but = button04; break; }
                case 4: { but = button05; break; }
                case 5: { but = button06; break; }
                case 6: { but = button07; break; }
                case 7: { but = button08; break; }
                case 8: { but = button09; break; }
                case 9: { but = button10; break; }
                case 10: { but = button11; break; }
                case 11: { but = button12; break; }
            }
            final Button finalBut = but;
            but.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalBut.setVisibility(View.VISIBLE);
                }
            }, secDelay);
            secDelay += 200;
        }
    }

    public void openRandomCard(int secDelay) {
        final int randomButtonIndex = (int)(Math.random()*(numberOfCards - 1));
        Button but = returnButton(randomButtonIndex);
        final Button finalBut = but;
        but.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalBut.setTextSize(60);
                finalBut.setText(emoji(game.cards.get(randomButtonIndex)));
                finalBut.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
        }, secDelay);
        secDelay += 400;
        but.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalBut.setTextSize(60);
                finalBut.setText("");
                finalBut.getBackground().setColorFilter(getResources().getColor(R.color.buttonsColor), PorterDuff.Mode.MULTIPLY);
            }
        }, secDelay);
    }

    public void updateViewFromModel() {
        for (int i = 0; i < numberOfCards; i++) {
            Card card = game.cards.get(i);
            functionForPressedButton(returnButton(i), card);
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

    String[] emojiChoices = {"🎃","👻","🎉","🐶","🐧","🏀","🏎","💎","🥑","🦇","🍎","🍏","🐧","🦅","🌏","☃️","🏁","💵","📱"};

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

    public Button returnButton(int index) {
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

        }
        return chosenButton;
    }
}
