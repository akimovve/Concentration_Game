package com.example.concentration.Game;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.concentration.DataSave.DataBaseHelper;
import com.example.concentration.HomeActivity;
import com.example.concentration.Info.Literals;
import com.example.concentration.LevelUpActivity;
import com.example.concentration.R;
import com.example.concentration.ResultsActivity;

import java.util.Objects;

public class ChallengeGameActivity extends GameAlgorithm {

    OnClickListener buttonClicks;
    TextView stopWatchText;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(this, "TableResultsChallenge", null, 1);
    Handler handler;
    long startTime, buffTime = 0L, resetTime, millisecTime;
    int seconds, minutes, milliSecs;
    private int flipCount = 0;
    private static int amountOfFlips = 0, allMistakes = 0;
    private boolean flag = true, homeButtonIsPressed = false;
    private static boolean pressedRestart = false;
    private final String LOG_TAG = "myLogs";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_layout);

        Bundle bundle = getIntent().getExtras();
        int connect = 0;
        if (bundle != null) {
            connect = bundle.getInt("whichLevel");
            flag = bundle.getBoolean("levelUp");
        }

        if (pressedRestart) {
            pressedRestart = false;
            flag = false;
        }

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        final int levelNumber;
        if (homeButtonIsPressed) {
            levelNumber = Literals.getLevelNumber(false);
            numberOfCards = Literals.getNumberOFButtons(false);
        } else {
            levelNumber = Literals.getLevelNumber(flag);
            numberOfCards = Literals.getNumberOFButtons(flag);
        }
        gameLogic = new SimilarGame((numberOfCards + 1) / 2);

        if (!flag || homeButtonIsPressed) {
            Literals.points = 0;
            amountOfFlips = 0;
            allMistakes = 0;
        }

        init();
        levelNumTextView.setText("Level " + levelNumber);

        setClick(false,1); // time for becoming cards not clickable
        appearanceOfCards(); // cards start to appear one by one
        openCardsRandomly(); // cards start opening randomly
        setClick(true, literals.delayForFirstAppearance + connect); // delay of start of the game
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
                handler.postDelayed(runnable, 0);
                startTime = SystemClock.uptimeMillis();
            }
        }, literals.delayForFirstAppearance + connect);


        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(ChallengeGameActivity.this, HomeActivity.class);
                intent.putExtra("homeButtonIsPressed", true);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });

        restartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pressedRestart = true;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        buttonClicks = new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                if (id != v.getId()) {
                    flipCount += 1;
                    amountOfFlips += 1;
                    id = v.getId();
                }
                flipsCountView.setText("Flips: " + flipCount);
                pointsView.setText("Points: " + gameLogic.mistakePoints);
                gameLogic.chooseCard(getIndex(v.getId()));
                updateViewFromModel();

                if (gameLogic.checkForAllMatchedCards()) {
                    Literals.points += Math.abs(gameLogic.mistakePoints) + flipCount;
                    allMistakes += gameLogic.mistakePoints;
                    if (levelNumber < Literals.maxLevel) {
                        buffTime += millisecTime;
                        handler.removeCallbacks(runnable);
                        Intent intent = new Intent(ChallengeGameActivity.this, LevelUpActivity.class);
                        intent.putExtra("flips", flipCount);
                        intent.putExtra("points", gameLogic.mistakePoints);
                        overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                        startActivity(intent);
                    } else {
                        buffTime += millisecTime;
                        handler.removeCallbacks(runnable);
                        showDialogModeSelector();
                    }
                }
            }
        };

        for (int index = 0; index < numberOfCards; index++) {
            Button btn = cards.get(index);
            if (btn.getId() - convertIdToIndex == index)
                btn.setOnClickListener(buttonClicks);
        }
    }

    private void init() {
        homeButtonIsPressed = false;
        flag = true;
        menuButton = findViewById(R.id.menuButton);
        restartButton = findViewById(R.id.restartButton);
        levelNumTextView = findViewById(R.id.levelTextView);
        flipsCountView = findViewById(R.id.flipsCountView);
        stopWatchText = findViewById(R.id.stopWatchText);
        pointsView = findViewById(R.id.pointsView);
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
        millisecTime = 0L;
        startTime = 0L;
        buffTime = 0L;
        resetTime = 0L;
        seconds = 0;
        minutes = 0;
        milliSecs = 0;
        stopWatchText.setText("00:00:00");
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecTime = SystemClock.uptimeMillis() - startTime;
            resetTime = buffTime + millisecTime;
            seconds = (int) (resetTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSecs = (int) (resetTime % 1000);
            stopWatchText.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSecs));
            handler.postDelayed(this, 0);
        }
    };

    private double round(int digit) {
        double result = Literals.getMaximumPoints()/digit;
        result *= 10000;
        int roundRes = (int) Math.round(result);
        return (double) roundRes/100;
    }

    private void showDialogModeSelector() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.form_filling_layout);

        final EditText nameEditText = dialog.findViewById(R.id.nameEditText);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1; // 1 - add, 2 - show, 3 - clear Data Base
                ContentValues contentValues = new ContentValues();
                String name = nameEditText.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
                    switch (a) {
                        case 1: {
                            Log.d(LOG_TAG, "--- INSERT in the table: ---");
                            contentValues.put("Name", name);
                            contentValues.put("Percents", round(Literals.points));
                            contentValues.put("Flips", amountOfFlips);
                            contentValues.put("Points", allMistakes);
                            long rowID = database.insert("TableResultsChallenge", null, contentValues);
                            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                            break;
                        }
                        case 2: {
                            Log.d(LOG_TAG, "--- READ the table: ---");
                            Cursor cursor = database.query("TableResultsChallenge", null, null, null, null, null, null);
                            if (cursor.moveToFirst()) {
                                int idColIndex = cursor.getColumnIndex("id");
                                int nameColIndex = cursor.getColumnIndex("Name");
                                int resultInPercents = cursor.getColumnIndex("Percents");
                                int resultInFlips = cursor.getColumnIndex("Flips");
                                int resultInPoints = cursor.getColumnIndex("Points");
                                do {
                                    Log.d(LOG_TAG, "ID = " + cursor.getInt(idColIndex)
                                                        + ", Name = " + cursor.getString(nameColIndex)
                                                        + ", Percents = " + cursor.getDouble(resultInPercents)
                                                        + ", Flips = " + cursor.getInt(resultInFlips)
                                                        + ", Points = " + cursor.getInt(resultInPoints));

                                } while (cursor.moveToNext());
                            } else
                                Log.d(LOG_TAG, "0 rows");
                            cursor.close();
                            break;
                        }
                        case 3: {
                            Log.d(LOG_TAG, "--- DELETE the table: ---");
                            int clear = database.delete("TableResultsChallenge", null, null);
                            Log.d(LOG_TAG, "deleted rows count = " + clear);
                            break;
                        }
                    }
                    dataBaseHelper.close();
                    Intent intent = new Intent(ChallengeGameActivity.this, ResultsActivity.class);
                    intent.putExtra("Results", true);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}
