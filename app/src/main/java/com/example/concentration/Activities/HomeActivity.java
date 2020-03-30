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

import com.example.concentration.DataSave.SharedPreferencesUtil;
import com.example.concentration.Game.ChallengeGameActivity;
import com.example.concentration.Game.MainGameActivity;
import com.example.concentration.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.logging.Logger;

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
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        Log.d(LOG_TAG, String.valueOf(acct));
        if (acct == null) showDialogToSignUp();
        else startGame(reset);
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

    private void startGame(boolean reset) {
        final Intent intent = new Intent(HomeActivity.this, ChallengeGameActivity.class);
        intent.putExtra("game_reset", reset);
        overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        },100);
    }

    private void showDialogToSignUp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pre_game_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.findViewById(R.id.signIn_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                intent.putExtra("sign_up", true);
                startActivity(intent);
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
        levelNumber = SharedPreferencesUtil.getUserLevel(this);
    }
}
