package com.example.concentration.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.data.SharedPreferencesUtil;
import com.example.concentration.game.ChallengeGameActivity;
import com.example.concentration.game.MainGameActivity;
import com.example.concentration.R;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private static boolean reset = false;
    private Animation myAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        myAnim = AnimationUtils.loadAnimation(this, R.anim.scale);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reset = bundle.getBoolean("game_reset");
        }


        TextView lvlTextView = findViewById(R.id.lvlTextView2);
        int levelNumber = SharedPreferencesUtil.getUserLevel(this);
        String lvl = String.valueOf(levelNumber);
        lvlTextView.setText(getResources().getText(R.string.score_0) + "  " + lvl);
    }

    public void startChallengeGame(View view) {
        view.startAnimation(myAnim);
        // GOOGLE
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        Log.d(LOG_TAG, String.valueOf(acct));

        // FACEBOOK
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (acct == null && !isLoggedIn) showDialogToSignUp();
        else startGame(reset);
    }

    public void startSingleGame(View view) {
        view.startAnimation(myAnim);
        Intent intent = new Intent(HomeActivity.this, MainGameActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void openRecordsView(View view) {
        view.startAnimation(myAnim);
        Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void openSettingsView(View view) {
        view.startAnimation(myAnim);
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void startGame(boolean reset) {
        final Intent intent = new Intent(HomeActivity.this, ChallengeGameActivity.class);
        intent.putExtra("game_reset", reset);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
