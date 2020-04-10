package com.example.concentration.game;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.concentration.activities.InfoActivity;
import com.example.concentration.data.DataBaseHelper;
import com.example.concentration.activities.HomeActivity;
import com.example.concentration.info.Literals;
import com.example.concentration.activities.LevelUpActivity;
import com.example.concentration.info.Post;
import com.example.concentration.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChallengeGameActivity extends GameAlgorithm {

    private static final String LOG_TAG = ChallengeGameActivity.class.getSimpleName();
    Handler handler;
    long buffTime = 0L, millisecTime;
    private int levelNumber, flipCount = 0;
    private static int amountOfFlips = 0, allMistakes = 0;
    private boolean isReset = false;
    private DatabaseReference mDatabase;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    OnClickListener onCardsPushed = new OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            final Animation myAnim = AnimationUtils.loadAnimation(ChallengeGameActivity.this, R.anim.alpha);
            if (id != v.getId()) {
                flipCount += 1;
                amountOfFlips += 1;
                id = v.getId();
            }
            flipsCountView.setText(getResources().getText(R.string.flips_0) + " " + flipCount);
            pointsView.setText(getResources().getText(R.string.points_0) + " " + gameLogic.mistakePoints);
            gameLogic.chooseCard(getIndex(v.getId()));
            updateViewFromModel();
            v.startAnimation(myAnim);

            if (gameLogic.checkForAllMatchedCards()) {
                Literals.points += Math.abs(gameLogic.mistakePoints) + flipCount;
                allMistakes += gameLogic.mistakePoints;
                if (levelNumber < Literals.maxLevel) {
                    buffTime += millisecTime;
                    handler.removeCallbacks(runnable);
                    Intent intent = new Intent(ChallengeGameActivity.this, LevelUpActivity.class);
                    intent.putExtra("game_reset", isReset);
                    intent.putExtra("flips", flipCount);
                    intent.putExtra("points", gameLogic.mistakePoints);
                    startActivity(intent);
                } else {
                    buffTime += millisecTime;
                    handler.removeCallbacks(runnable);

                    showDialogModeSelector();
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_layout);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) isReset = bundle.getBoolean("game_reset");

        gameReset(isReset, bundle);
        gameLogic = new QuickEyeGame((numberOfCards + 1) / 2);
        init();
        gameSetUp();
        startTimer();
        initButtonsOnClick();
    }

    private void gameReset(boolean reset, Bundle bundle) {
        if (reset) {
            levelNumber = Literals.getLevelNumber(false);
            numberOfCards = Literals.getNumberOFButtons(false);
            Literals.points = 0;
            amountOfFlips = 0;
            allMistakes = 0;
            speed = 0;
        } else {
            levelNumber = Literals.getLevelNumber(true);
            numberOfCards = Literals.getNumberOFButtons(true);
            if (bundle != null) speed = bundle.getInt("speed");
        }
    }

    private void gameSetUp() {
        setClick(false,1); // time for becoming cards not clickable
        appearanceOfCards(); // cards start to appear one by one
        openCardsRandomly(); // cards start opening randomly
        setClick(true, literals.delayForFirstAppearance + speed); // delay of start of the game
    }

    private void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
                handler.postDelayed(runnable, 0);
                startTime = SystemClock.uptimeMillis();
            }
        }, literals.delayForFirstAppearance + speed);
    }

    private void initButtonsOnClick() {
        for (int i = 0; i < numberOfCards; i++) {
            Button btn = cards.get(i);
            if (btn.getId() - convertIdToIndex == i)
                btn.setOnClickListener(onCardsPushed);
        }
    }


    TextView stopWatchText;
    int seconds, minutes, milliSecs;
    long startTime, resetTime;

    private void init() {
        levelNumTextView = findViewById(R.id.levelTextView);
        flipsCountView = findViewById(R.id.flipsCountView);
        stopWatchText = findViewById(R.id.stopWatchText);
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
        millisecTime = 0L; startTime = 0L; buffTime = 0L; resetTime = 0L;
        seconds = 0; minutes = 0; milliSecs = 0;
        stopWatchText.setText("00:00:00");
        levelNumTextView.setText(getResources().getText(R.string.lvl) + " " + levelNumber);
    }

    public void openHomeMenu(View view) {
        Intent replyIntent = new Intent(ChallengeGameActivity.this, HomeActivity.class);
        finish();
        replyIntent.putExtra("game_reset", true);
        startActivity(replyIntent);
    }

    public void restartGame(View view) {
        Intent intent = getIntent();
        finish();
        intent.putExtra("game_reset", true);
        startActivity(intent);
    }

    private Runnable runnable = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
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


    DataBaseHelper dataBaseHelper = new DataBaseHelper(this, "GameRes", null, 1);

    private void showDialogModeSelector() {
        final String LOG_TAG_DB = "DataBase";

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_win);

        final EditText nameEditText = dialog.findViewById(R.id.nameEditText);

        nameEditText.requestFocus();
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nameEditText.setHint(user.getDisplayName());
        }

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().isEmpty()?
                        user.getDisplayName() : nameEditText.getText().toString();

                final String uid = getUid();

                addPost(uid, name);

                ContentValues contentValues = new ContentValues();
                SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

                Log.d(LOG_TAG_DB, "--- INSERT in the table: ---");
                contentValues.put("Name", name);
                contentValues.put("Percents", round(Literals.points));
                contentValues.put("Flips", amountOfFlips);
                contentValues.put("Points", allMistakes);
                long rowID = database.insert("GameRes", null, contentValues);
                Log.d(LOG_TAG_DB, "row inserted, ID = " + rowID);

                nameEditText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                dataBaseHelper.close();
                Intent intent = new Intent(ChallengeGameActivity.this, InfoActivity.class);
                intent.putExtra("Results", true);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addPost(final String userId, final String username) {
        mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (user == null) {
                    Log.e(LOG_TAG, "User " + userId + " is unexpectedly null");
                    Toast.makeText(ChallengeGameActivity.this, "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    writeNewPost(userId, username, String.valueOf(round(Literals.points)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(LOG_TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void writeNewPost(String userId, String name, String percents) {
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, name, percents);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Log.d(LOG_TAG, String.valueOf(postValues));
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
