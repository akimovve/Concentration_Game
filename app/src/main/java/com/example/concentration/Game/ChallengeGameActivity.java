package com.example.concentration.Game;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.concentration.Info.Literals;
import com.example.concentration.LevelUpActivity;
import com.example.concentration.PauseActivity;
import com.example.concentration.R;
import com.example.concentration.ResultsActivity;
import java.util.Objects;

public class ChallengeGameActivity extends GameClass {

    OnClickListener buttonClicks;
    DBHelper dbHelper;
    private int flipCount = 0;
    private static int amountOfFlips = 0;
    private boolean flag = true, homeButtonIsPressed = false;
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
            homeButtonIsPressed = bundle.getBoolean("isHomButPressed");
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

        gameLogic = new Focus((numberOfCards + 1) / 2);

        if (!flag) amountOfFlips = 0;

        init();
        levelNumTextView.setText("Level " + levelNumber);

        setClick(false,1); // time for becoming cards not clickable
        appearanceOfCards(); // cards start to appear one by one
        openCardsRandomly(); // cards start opening randomly
        setClick(true, literals.delayForFirstAppearance + connect); // delay of start of the game

        pauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeGameActivity.this, PauseActivity.class);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
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
                pointsView.setText("Points: " + gameLogic.points);
                gameLogic.chooseCard(getIndex(v.getId()));
                updateViewFromModel();

                if (gameLogic.checkForAllMatchedCards()) {
                    if (levelNumber < Literals.maxLevel) {
                        Intent intent = new Intent(ChallengeGameActivity.this, LevelUpActivity.class);
                        intent.putExtra("number_of_flips", flipCount);
                        overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                        startActivity(intent);
                    } else {
                        showDialogModeSelector();
                    }
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
        homeButtonIsPressed = false;
        flag = true;
        dbHelper = new DBHelper(this); // creating object for Data Base
        pauseButton = findViewById(R.id.pauseButton);
        levelNumTextView = findViewById(R.id.levelTextView);
        flipsCountView = findViewById(R.id.flipsCountView);
        pointsView = findViewById(R.id.pointsView);
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
                ContentValues cv = new ContentValues();
                String name = nameEditText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase(); // connecting to Data Base (insert – вставка, query – чтение, delete – удаление)

                    switch (a) {
                        case 1: {
                            Log.d(LOG_TAG, "--- INSERT in the table: ---");
                            cv.put("name", name);
                            cv.put("flips", amountOfFlips);
                            long rowID = db.insert("results_table", null, cv);
                            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                            break;
                        }
                        case 2: {
                            Log.d(LOG_TAG, "--- READ the table: ---"); // Делаем запрос всех данных из таблицы results_table, получаем Cursor
                            Cursor c = db.query("results_table", null, null, null, null, null, null);

                            // Ставим позицию курсора на первую строку выборки. Если в выборке нет строк, вернётся false
                            if (c.moveToFirst()) {
                                int idColIndex = c.getColumnIndex("id"); // Номера столбцов по имени в выборке
                                int nameColIndex = c.getColumnIndex("name");
                                int flipsColIndex = c.getColumnIndex("flips");

                                do {
                                    // Получаем значения по номерам столбцов и пишем все в лог
                                    Log.d(LOG_TAG,
                                            "ID = " + c.getInt(idColIndex) +
                                                    ", name = " + c.getString(nameColIndex) +
                                                    ", flips = " + c.getInt(flipsColIndex));

                                } while (c.moveToNext()); // Переход на следующую строку, а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } else
                                Log.d(LOG_TAG, "0 rows");
                            c.close();
                            break;
                        }
                        case 3: {
                            Log.d(LOG_TAG, "--- DELETE the table: ---");
                            int clearCount = db.delete("results_table", null, null); // Удаляем всё
                            Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                            break;
                        }
                    }
                    dbHelper.close();

                    Intent intent = new Intent(ChallengeGameActivity.this, ResultsActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) { // Конструктор суперкласса
            super(context, "resultsDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            Log.d(LOG_TAG, "--- onCreate database ---"); // Создаем таблицу с полями
            database.execSQL("create table results_table ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "flips text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
