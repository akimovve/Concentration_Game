package com.example.concentration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.concentration.Info.Constants;
import com.example.concentration.Levels.LevelUpActivity;
import com.example.concentration.Menu.HomeActivity;
import com.example.concentration.Menu.PauseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GamePlayActivity extends AppCompatActivity {

    int numberOfButtons = 0;
    int flipCount = 0;
    int id = -1;
    int connect = 0;
    final int maxLevel = 5;
    final int convertIdToIndex = R.id.button_00;
    boolean flag = true;
    boolean homeButtonIsPressed = false;

    Constants constants = new Constants();
    OnClickListener onButtonsClick;
    Concentration game;

    private TextView flipsCountView;
    Button pauseButton;
    ArrayList<Button> buttons = new ArrayList<>();

    DBHelper dbHelper;
    final String LOG_TAG = "myLogs"; // logging

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            connect = bundle.getInt("whichLevel");
            flag = bundle.getBoolean("levelUp");
            homeButtonIsPressed = bundle.getBoolean("isHomButPressed");
        }

        final int levelNumber;
        if (homeButtonIsPressed) {
            levelNumber = Constants.getLevelNumber(false);
            numberOfButtons = Constants.getNumberOFButtons(false);
        } else {
            levelNumber = Constants.getLevelNumber(flag);
            numberOfButtons = Constants.getNumberOFButtons(flag);
        }

        game = new Concentration((numberOfButtons + 1) / 2);

        flipsCountView = findViewById(R.id.flipsCountView);
        TextView levelTextView = findViewById(R.id.levelTextView);
        pauseButton = findViewById(R.id.pauseButton);

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

        dbHelper = new DBHelper(this); // creating object for Data Base

        levelTextView.setText("Level " + levelNumber);

        setClick(false,1); // time for becoming cards not clickable
        appearanceOfCards(); // cards start to appear one by one
        openCardsRandomly(); // cards start opening randomly
        setClick(true, constants.delayForFirstAppearance + connect); // delay of start of the game

        onButtonsClick = new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (id != v.getId()) {
                    flipCount += 1;
                    id = v.getId();
                }
                flipsCountView.setText("Flips: " + flipCount);
                game.chooseCard(getIndex(v.getId()));
                updateViewFromModel();

                if (!game.checkForAllMatchedCards()) {
                    if (levelNumber < maxLevel) {
                        Intent intent = new Intent(GamePlayActivity.this, LevelUpActivity.class);
                        intent.putExtra("number_of_flips", flipCount);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                    } else {
                        showDialogModeSelector();

                    }
                }
            }
        };

        for (int index = 0; index < numberOfButtons; index++)
            createButtons(buttons.get(index),index);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamePlayActivity.this, PauseActivity.class);
                startActivity(intent);
            }
        });
    }



    public void createButtons(Button button, int num) {
        if (button.getId() - convertIdToIndex == num)
            button.setOnClickListener(onButtonsClick);
    }


    public void appearanceOfCards() {
        for (int index = 0; index < numberOfButtons; index++) {
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
        for (int k = 0; k < numberOfButtons; k++) {
            checkTheRepeat.put(k, false); // all buttons haven't opened yet => false
        }

        ArrayList<Integer> randArrOfFirstIndexes = new ArrayList<>(); // array of random sequence of cards' indexes
        for (int i = 0; i < numberOfButtons; i++)
            randArrOfFirstIndexes.add(i);

        Collections.shuffle(randArrOfFirstIndexes);

        if (numberOfButtons == 4) {
            int deleteRandom = (int)(Math.random() * 4);
            randArrOfFirstIndexes.remove(deleteRandom);
        } else {
            int[] secondRandArray = new int[(int) (Math.random() * (numberOfButtons / 3) + (numberOfButtons / 3))]; // random size [(numberOfCards / 4);(numberOfCards/2)]
            for (int i = 0; i < secondRandArray.length; i++) {
                int randomIndexOfFirstArray;
                do {
                    randomIndexOfFirstArray = (int) (Math.random() * numberOfButtons);
                } while (checkTheRepeat.get(randomIndexOfFirstArray));
                secondRandArray[i] = randArrOfFirstIndexes.get(randomIndexOfFirstArray);
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
        for (int rIndex = 0; rIndex < array.size(); rIndex++) {
            final int randomButtonIndex = array.get(rIndex);
            final Button finalBut = pressedButton(randomButtonIndex);
            finalBut.setTextSize(40);
            finalBut.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalBut.setText(getEmoji(game.cards.get(randomButtonIndex))); // opened
                    finalBut.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                }
            }, constants.delayForFirstAppearance-connect); // default. DO NOT TOUCH!
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


    public void setClick(final Boolean fl, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int index = 0; index < numberOfButtons; index++)
                    buttons.get(index).setClickable(fl);
            }
        }, delay);
    }


    public void updateViewFromModel() {
        for (int index = 0; index < numberOfButtons; index++) {
            Card card = game.cards.get(index);
            functionForPressedButton(buttons.get(index), card);
        }
    }


    public void functionForPressedButton(Button button, Card card) {
        button.setTextSize(40);
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


    String[] emojiTypes = {"🏰","🏁","💂","💍","🐒","🦞","🎄","🏍","👾","🦁","🐿","🔥","🌘","🍕","⚽️","🥁","🧀","🛩","📸","🎁","🍏",
            "🌈","🎮️","🌶","🪁","🚔","🎡","🏔","🚄","🎬","🐙","🍄","🌵","🐢","👑","🧞","👻","🧤","🎓","🎪","🐶","🐲","🍓","🧁","🏆","🎰" };
    int howShorter = 0;

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


    public Button pressedButton(int index) { return buttons.get(index); }


    @SuppressLint("ResourceType")
    public int getIndex(int index) { return index-convertIdToIndex; }


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
                SQLiteDatabase db = dbHelper.getWritableDatabase(); // connecting to Data Base (insert – вставка, query – чтение, delete – удаление)

                switch (a) {
                    case 1: {
                        Log.d(LOG_TAG, "--- INSERT in the table: ---");
                        cv.put("name", name);
                        cv.put("flips", flipCount);
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

                Intent intent = new Intent(GamePlayActivity.this, ResultsActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "resultsDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
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
