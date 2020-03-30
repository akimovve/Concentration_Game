package com.example.concentration.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.DataSave.PreferencesUtil;
import com.example.concentration.Game.ChallengeGameActivity;
import com.example.concentration.Game.MainGameActivity;
import com.example.concentration.R;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private int levelNumber = 1;
    private TextView lvlTextView;
    private AnimationDrawable mAnimationDrawable;
    private static boolean reset = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reset = bundle.getBoolean("game_reset");
        }

        init();
        readDB();
        String lvl = String.valueOf(levelNumber);
        lvlTextView.setText(getResources().getText(R.string.score_0) + "  " + lvl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnimationDrawable.start();
    }

    public void startChallengeGame(View view) {
        showDialogModeSelector(reset);
    }

    public void startSingleGame(View view) {
        Intent intent = new Intent(HomeActivity.this, MainGameActivity.class);
        startActivity(intent);
    }

    public void openRewardsView(View view) {
        Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void openRecordsView(View view) {
        Intent intent = new Intent(HomeActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

    public void openPresentsView(View view) {
        Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
    }

    public void openSettingsView(View view) {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void showDialogModeSelector(final boolean fl) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.precompetition_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.findViewById(R.id.startCompetition).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(HomeActivity.this, ChallengeGameActivity.class);
                intent.putExtra("game_reset", fl);
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
        LinearLayout layout = findViewById(R.id.main_linear);
        mAnimationDrawable = (AnimationDrawable) layout.getBackground();
        mAnimationDrawable.setEnterFadeDuration(100);
        mAnimationDrawable.setExitFadeDuration(4000);
        lvlTextView = findViewById(R.id.lvlTextView);
    }

    private void readDB() {
        levelNumber = PreferencesUtil.getUserLevel(this);
    }
}
