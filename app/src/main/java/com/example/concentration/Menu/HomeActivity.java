package com.example.concentration.Menu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.CompetitionGameActivity;
import com.example.concentration.PreferencesUtil;
import com.example.concentration.R;
import com.example.concentration.ResultsActivity;
import com.example.concentration.SettingsActivity;
import com.example.concentration.UnlimitedGameActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    boolean isHomeButtonPressed = false;
    int levelNumber = 1;
    Button rewardsButton, challengeButton, tableOfRecordsButton, settingsButton, mainPlayButton, presentsButton;
    TextView lvlTextView;
    LinearLayout layout;
    private AnimationDrawable mAnimationDrawable;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);

        init();
        readDB();
        String lvl = String.valueOf(levelNumber);
        lvlTextView.setText("SCORE " + lvl);

        tableOfRecordsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });

        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                final Intent intent = new Intent(HomeActivity.this, CompetitionGameActivity.class);
                intent.putExtra("isHomButPressed", isHomeButtonPressed);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                },100);
            }
        });

        mainPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, UnlimitedGameActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
            }
        };
        rewardsButton.setOnClickListener(onClickListener);
        presentsButton.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnimationDrawable.start();
    }

    private void init() {
        layout = findViewById(R.id.main_linear);
        mAnimationDrawable = (AnimationDrawable) layout.getBackground();
        mAnimationDrawable.setEnterFadeDuration(100);
        mAnimationDrawable.setExitFadeDuration(4000);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) { isHomeButtonPressed = bundle.getBoolean("isHomeButtonPressed"); }
        rewardsButton = findViewById(R.id.rewardsButton);
        challengeButton = findViewById(R.id.challengeButton);
        tableOfRecordsButton = findViewById(R.id.tableOfRecordsButton);
        settingsButton = findViewById(R.id.pauseButton);
        presentsButton = findViewById(R.id.presentsButton);
        mainPlayButton = findViewById(R.id.mainPlayButton);
        lvlTextView = findViewById(R.id.lvlTextView);
    }

    private void readDB() {
        levelNumber = PreferencesUtil.getUserLevel(this);
    }
}
