package com.example.concentration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.example.concentration.Game.ChallengeGameActivity;
import com.example.concentration.Game.MainGameActivity;
import com.example.concentration.Info.Variables;

public class LevelUpActivity extends Activity {

    private Button nextButton, homeButton;
    private TextView levelPassedTextView;
    private Intent intent;
    private Variables var = new Variables();
    private boolean whichActivity;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelup_layout);
        whichActivity = false;

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final boolean reset = bundle.getBoolean("reset_game");
        int flipsNum = bundle.getInt("flips");
        int points = bundle.getInt("points");
        whichActivity = bundle.getBoolean("activity");

        init();
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);
        levelPassedTextView.setText(getResources().getText(R.string.matched_text1) + "\n" + flipsNum + " "
                                    + getResources().getText(R.string.matched_text2) + "\n" + points + " "
                                    + getResources().getText(R.string.matched_text3));

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(LevelUpActivity.this, HomeActivity.class);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                intent.putExtra("reset_game", true);
                startActivity(intent);
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                if (whichActivity) {
                    intent = new Intent(LevelUpActivity.this, MainGameActivity.class);
                } else intent = new Intent(LevelUpActivity.this, ChallengeGameActivity.class);
                intent.putExtra("speed", var.setChange(!reset));
                intent.putExtra("levelUp",true);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });
    }

    private void init() {
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);
        levelPassedTextView = findViewById(R.id.levelPassedTextView);
    }
}
