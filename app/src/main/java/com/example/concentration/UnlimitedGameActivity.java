package com.example.concentration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.concentration.Levels.LevelUpActivity;
import com.example.concentration.Menu.PauseActivity;

import java.util.Random;

public class UnlimitedGameActivity extends Game {

    OnClickListener buttonClicks;
    int flipCount = 0;
    static int levelNumber;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_layout);

        numberOfCards = 16;
        gameLogic = new Concentration((numberOfCards + 1) / 2);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        init();
        levelNumber = PreferencesUtil.getUserLevel(this);
        levelNumTextView.setText("Level "+ levelNumber);

        setClick(false,1); // time for becoming cards not clickable
        appearanceOfCards(); // cards start to appear one by one
        openCardsRandomly(); // cards start opening randomly
        setClick(true, constants.delayForFirstAppearance + connect); // delay of start of the game

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnlimitedGameActivity.this, PauseActivity.class);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                intent.putExtra("activity", true);
                startActivity(intent);
            }
        });

        buttonClicks = new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                if (id != v.getId()) {
                    flipCount += 1;
                    id = v.getId();
                }
                flipsCountView.setText(String.valueOf(flipCount));

                gameLogic.chooseCard(getIndex(v.getId()));
                updateViewFromModel();

                if (!gameLogic.checkForAllMatchedCards()) {
                    levelNumber += 1;
                    PreferencesUtil.saveUserLevel(UnlimitedGameActivity.this, levelNumber);
                    Intent intent = new Intent(UnlimitedGameActivity.this, LevelUpActivity.class);
                    intent.putExtra("number_of_flips", flipCount);
                    intent.putExtra("activity", true);
                    overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                    startActivity(intent);
                }
            }
        };

        for (int index = 0; index < numberOfCards; index++) {
            Button btn = buttons.get(index);
            if (btn.getId() - convertIdToIndex == index)
                btn.setOnClickListener(buttonClicks);
        }
    }

    private void init() {
        pauseButton = findViewById(R.id.pauseButton);
        levelNumTextView = findViewById(R.id.levelTextView);
        flipsCountView = findViewById(R.id.flipsCountView);
        buttons.add((Button)findViewById(R.id.button_00));
        buttons.add((Button)findViewById(R.id.button_01));
        buttons.add((Button)findViewById(R.id.button_02));
        buttons.add((Button)findViewById(R.id.button_03));
        buttons.add((Button)findViewById(R.id.button_04));
        buttons.add((Button)findViewById(R.id.button_05));
        buttons.add((Button)findViewById(R.id.button_06));
        buttons.add((Button)findViewById(R.id.button_07));
        buttons.add((Button)findViewById(R.id.button_08));
        buttons.add((Button)findViewById(R.id.button_09));
        buttons.add((Button)findViewById(R.id.button_10));
        buttons.add((Button)findViewById(R.id.button_11));
        buttons.add((Button)findViewById(R.id.button_12));
        buttons.add((Button)findViewById(R.id.button_13));
        buttons.add((Button)findViewById(R.id.button_14));
        buttons.add((Button)findViewById(R.id.button_15));
        buttons.add((Button)findViewById(R.id.button_16));
        buttons.add((Button)findViewById(R.id.button_17));
        buttons.add((Button)findViewById(R.id.button_18));
        buttons.add((Button)findViewById(R.id.button_19));
    }
}
