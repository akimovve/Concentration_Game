package com.example.concentration;

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

import com.example.concentration.DataSave.PreferencesUtil;
import com.example.concentration.Game.ChallengeGameActivity;
import com.example.concentration.Game.MajorGameActivity;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private boolean isHomeButtonPressed = false;
    private int levelNumber = 1;
    private Button rewardsButton, challengeButton, tableOfRecordsButton, settingsButton, mainPlayButton, presentsButton;
    private TextView lvlTextView;
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
                showDialogModeSelector();
            }
        });

        mainPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, MajorGameActivity.class);
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
    protected void onPause() {
        super.onPause();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnimationDrawable.start();
    }

    private void showDialogModeSelector() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.precompetition_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.findViewById(R.id.startCompetition).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(HomeActivity.this, ChallengeGameActivity.class);
                intent.putExtra("isHomButPressed", isHomeButtonPressed);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        dialog.dismiss();
                    }
                },100);
            }
        });
        dialog.show();
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
        settingsButton = findViewById(R.id.menuButton);
        presentsButton = findViewById(R.id.presentsButton);
        mainPlayButton = findViewById(R.id.mainPlayButton);
        lvlTextView = findViewById(R.id.lvlTextView);
    }

    private void readDB() {
        levelNumber = PreferencesUtil.getUserLevel(this);
    }
}
