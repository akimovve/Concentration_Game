package com.example.concentration.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.example.concentration.DataSave.SharedPreferencesUtil;
import com.example.concentration.Game.ChallengeGameActivity;
import com.example.concentration.Game.MainGameActivity;
import com.example.concentration.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private int levelNumber = 1;
    private TextView lvlTextView;
    private static boolean reset = false;
    private GestureDetectorCompat mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mGestureDetector = new GestureDetectorCompat(this, new GestureListener());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reset = bundle.getBoolean("game_reset");
        }

        init();
        readDB();
        String lvl = String.valueOf(levelNumber);
        lvlTextView.setText(getResources().getText(R.string.score_0) + "  " + lvl);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getY() < e1.getY() && Math.abs(e1.getX() - e2.getX()) <= 50) {
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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
        Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
    }

    public void openRecordsView(View view) {
        Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
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
        lvlTextView = findViewById(R.id.lvlTextView);
    }

    private void readDB() {
        levelNumber = SharedPreferencesUtil.getUserLevel(this);
    }
}
