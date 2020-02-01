package com.example.concentration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int numberOfCards = 4;
    Concentration game = new Concentration((numberOfCards + 1) / 2);
    private TextView flipsCountView;
    private Button button01, button02, button03, button04;
    int flipCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipsCountView = findViewById(R.id.flipsCountView);
        button01 = findViewById(R.id.button01);
        button02 = findViewById(R.id.button02);
        button03 = findViewById(R.id.button03);
        button04 = findViewById(R.id.button04);

        OnClickListener onButtonsClick = new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                flipCount += 1;
                flipsCountView.setText("Flips: " + flipCount);
                switch (v.getId()) {
                    case R.id.button01: { game.chooseCard(0); break; }
                    case R.id.button02: { game.chooseCard(1); break; }
                    case R.id.button03: { game.chooseCard(2); break; }
                    case R.id.button04: { game.chooseCard(3); break; }
                }
                updateViewFromModel();
            }
        };
        button01.setOnClickListener(onButtonsClick);
        button02.setOnClickListener(onButtonsClick);
        button03.setOnClickListener(onButtonsClick);
        button04.setOnClickListener(onButtonsClick);
    }



    public void updateViewFromModel() {
        for (int i = 0; i < numberOfCards; i++) {
            Card card = game.cards[i];
            switch (i) {
                case 0:
                    functionForPressedButton(button01, card);
                    break;
                case 1:
                    functionForPressedButton(button02, card);
                    break;
                case 2:
                    functionForPressedButton(button03, card);
                    break;
                case 3:
                    functionForPressedButton(button04, card);
                    break;
            }

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
            }
        }
    }

    String[] emojiChoices = {"🎃","👻","🎉","🐶","🐧","🏀","🏎","💎","🥑","🦇","🍎"};

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
}
