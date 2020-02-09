package com.example.concentration;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.concentration.Levels.LevelUpActivity;
import com.example.concentration.Menu.PauseActivity;

public class UnlimitedGameActivity extends Game {

    OnClickListener buttonClicks;
    int flipCount = 0;
    static int levelNumber;
    DataBaseHelper dataBaseHelper;
    ContentValues cv;
    SQLiteDatabase db;
    Cursor c;
    final String LOG_TAG = "myLogs";


    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_layout);

        numberOfCards = 16;
        gameLogic = new Concentration((numberOfCards + 1) / 2);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        init();
        readDB();

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
                flipsCountView.setText("Flips: " + flipCount);

                gameLogic.chooseCard(getIndex(v.getId()));
                updateViewFromModel();

                if (!gameLogic.checkForAllMatchedCards()) {
                    levelNumber += 1;
                    insertDB();
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
        c.close();
    }

    private void init() {
        dataBaseHelper = new DataBaseHelper(this);
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

    private void readDB() {
        db = dataBaseHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- READ the table: ---");
        c = db.query("table_levels", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int levelColIndex = c.getColumnIndex("level");

            do {
                if (c.isLast()) levelNumber = c.getInt(levelColIndex);
                Log.d(LOG_TAG,
                        "LAST: ID = " + c.getInt(idColIndex) +
                                ", level = " + c.getInt(levelColIndex));
            } while (c.moveToNext());
        } else {
            levelNumber = 1;
            Log.d(LOG_TAG, "0 rows");
        }
    }

    private void insertDB() {
        cv = new ContentValues();
        Log.d(LOG_TAG, "--- INSERT in the table: ---");
        cv.put("level", levelNumber);
        long rowID = db.insert("table_levels", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    }

    class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, "DataBaseLevel", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            db.execSQL("create table table_levels (" + "id integer primary key autoincrement," + "level integer" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
