package com.example.concentration.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reset = bundle.getBoolean("game_reset");
        }


        lvlTextView = findViewById(R.id.lvlTextView2);
        levelNumber = SharedPreferencesUtil.getUserLevel(this);
        String lvl = String.valueOf(levelNumber);
        lvlTextView.setText(getResources().getText(R.string.score_0) + "  " + lvl);
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

    public void openRecordsView(View view) {
        Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void openSettingsView(View view) {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void startGame(boolean reset) {
        final Intent intent = new Intent(HomeActivity.this, ChallengeGameActivity.class);
        intent.putExtra("game_reset", reset);
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
        dialog.setContentView(R.layout.dialog_authentication);
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
}
