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
import com.example.concentration.Game.MajorGameActivity;
import com.example.concentration.Info.Variables;

public class LevelUpActivity extends Activity {

    private Button restartButton, nextButton, homeButton;
    private TextView levelPassedTextView;
    private Intent intent;
    private Variables var;
    private boolean whichActivity;
    private int flipsNum = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelup_layout);
        whichActivity = false;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            whichActivity = bundle.getBoolean("activity");
            flipsNum = bundle.getInt("number_of_flips");
        }

        init();
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);
        levelPassedTextView.setText("You found pairs in " + flipsNum + " flips!");

        OnClickListener onButtonClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                boolean flag = true;
                if (v.getId() == R.id.restartButton) flag = false;
                var = new Variables(flag); // check if the user tapped "next" or "previous" for changing level number and delay
                if (whichActivity) {
                    intent = new Intent(LevelUpActivity.this, MajorGameActivity.class);
                } else intent = new Intent(LevelUpActivity.this, ChallengeGameActivity.class);
                intent.putExtra("whichLevel", var.changeDelay);
                intent.putExtra("levelUp",flag);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        };

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(LevelUpActivity.this, HomeActivity.class);
                intent.putExtra("homeButtonIsPressed", true);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });

        restartButton.setOnClickListener(onButtonClick);
        nextButton.setOnClickListener(onButtonClick);
    }

    private void init() {
        restartButton = findViewById(R.id.restartButton);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);
        levelPassedTextView = findViewById(R.id.levelPassedTextView);
    }
}
