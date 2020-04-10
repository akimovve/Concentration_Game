package com.example.concentration.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.Nullable;

import com.example.concentration.activities.HomeActivity;
import com.example.concentration.activities.LevelUpActivity;
import com.example.concentration.data.SharedPreferencesUtil;
import com.example.concentration.R;

public class MainGameActivity extends GameAlgorithm {

    OnClickListener buttonClicks;
    private int flipCount = 0;
    private static int levelNumber;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_layout);

        numberOfCards = 16;
        gameLogic = new QuickEyeGame((numberOfCards + 1) / 2);

        init();
        levelNumber = SharedPreferencesUtil.getUserLevel(this);
        levelNumTextView.setText(getResources().getText(R.string.lvl) + " " + levelNumber);

        setClick(false,1); // time for becoming cards not clickable
        appearanceOfCards(); // cards start to appear one by one
        openCardsRandomly(); // cards start opening randomly
        setClick(true, literals.delayForFirstAppearance); // delay of start of the game

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGameActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        buttonClicks = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != v.getId()) {
                    flipCount += 1;
                    id = v.getId();
                }
                flipsCountView.setText("Flips: " + flipCount);
                pointsView.setText("Points: " + gameLogic.mistakePoints);
                gameLogic.chooseCard(getIndex(v.getId()));
                System.out.println(gameLogic.mistakePoints);
                updateViewFromModel();

                if (gameLogic.checkForAllMatchedCards()) {
                    levelNumber += 1;
                    Intent intent = new Intent(MainGameActivity.this, LevelUpActivity.class);
                    intent.putExtra("flips", flipCount);
                    intent.putExtra("points", gameLogic.mistakePoints);
                    intent.putExtra("activity", true);
                    startActivity(intent);
                }
            }
        };

        for (int index = 0; index < numberOfCards; index++) {
            Button btn = cards.get(index);
            if (btn.getId() - convertIdToIndex == index)
                btn.setOnClickListener(buttonClicks);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesUtil.saveUserLevel(MainGameActivity.this, levelNumber);
    }

    private void init() {
        menuButton = findViewById(R.id.menuButton);
        restartButton = findViewById(R.id.restartButton);
        levelNumTextView = findViewById(R.id.levelTextView);
        flipsCountView = findViewById(R.id.flipsCountView);
        pointsView = findViewById(R.id.pointsView);
        flipsCountView.setText(getResources().getText(R.string.flips_0) + " 0");
        pointsView.setText(getResources().getText(R.string.points_0) + " 0");
        cards.add((Button)findViewById(R.id.button_00));
        cards.add((Button)findViewById(R.id.button_01));
        cards.add((Button)findViewById(R.id.button_02));
        cards.add((Button)findViewById(R.id.button_03));
        cards.add((Button)findViewById(R.id.button_04));
        cards.add((Button)findViewById(R.id.button_05));
        cards.add((Button)findViewById(R.id.button_06));
        cards.add((Button)findViewById(R.id.button_07));
        cards.add((Button)findViewById(R.id.button_08));
        cards.add((Button)findViewById(R.id.button_09));
        cards.add((Button)findViewById(R.id.button_10));
        cards.add((Button)findViewById(R.id.button_11));
        cards.add((Button)findViewById(R.id.button_12));
        cards.add((Button)findViewById(R.id.button_13));
        cards.add((Button)findViewById(R.id.button_14));
        cards.add((Button)findViewById(R.id.button_15));
        cards.add((Button)findViewById(R.id.button_16));
        cards.add((Button)findViewById(R.id.button_17));
        cards.add((Button)findViewById(R.id.button_18));
        cards.add((Button)findViewById(R.id.button_19));
        restartButton.setVisibility(View.INVISIBLE);
    }
}
