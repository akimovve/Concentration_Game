package com.example.concentration.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.concentration.game.ChallengeGameActivity;
import com.example.concentration.game.MainGameActivity;
import com.example.concentration.info.Variables;
import com.example.concentration.R;

public class LevelUpActivity extends Activity {

    private static final String LOG_TAG = LevelUpActivity.class.getSimpleName();
    private Variables var = new Variables();
    private boolean whichActivity = false;
    private boolean isReset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextlvl_layout);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        isReset = bundle.getBoolean("game_reset");
        int flipsNum = bundle.getInt("flips");
        int points = bundle.getInt("points");
        whichActivity = bundle.getBoolean("activity");

        init(flipsNum, points);
    }

    @SuppressLint("SetTextI18n")
    private void init(int flipsNum, int points) {
        TextView levelPassedTextView = findViewById(R.id.levelPassedTextView);
        levelPassedTextView.setText(getResources().getText(R.string.matched_text1) + "\n"
                        + flipsNum + " " + getResources().getText(R.string.matched_text2) + "\n"
                        + points + " " + getResources().getText(R.string.matched_text3));
    }

    public void openHomeMenu(View view) {
        Intent replyIntent = new Intent(LevelUpActivity.this, HomeActivity.class);
        finish();
        replyIntent.putExtra("game_reset", true);
        startActivity(replyIntent);
    }

    public void nextLevel(View view) {
        Intent intent;
        if (whichActivity) {
            Log.d(LOG_TAG, "main game mode opened");
            intent = new Intent(LevelUpActivity.this, MainGameActivity.class);
        } else {
            Log.d(LOG_TAG, "challenge game mode opened");
            intent = new Intent(LevelUpActivity.this, ChallengeGameActivity.class);
        }
        intent.putExtra("speed", var.setChange(!isReset));
        intent.putExtra("levelUp",true);
        startActivity(intent);
    }
}
