package com.example.concentration.Menu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.GamePlayActivity;
import com.example.concentration.R;
import com.example.concentration.ResultsActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    boolean isHomeButtonPressed = false;
    Button rewardsButton, gameModeButton, tableOfRecordsButton, settingsButton, mainPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) { isHomeButtonPressed = bundle.getBoolean("isHomeButtonPressed"); }

        rewardsButton = findViewById(R.id.rewardsButton);
        gameModeButton = findViewById(R.id.gameModeButton);
        tableOfRecordsButton = findViewById(R.id.tableOfRecordsButton);
        settingsButton = findViewById(R.id.pauseButton);
        mainPlayButton = findViewById(R.id.mainPlayButton);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);

        tableOfRecordsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });

        gameModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                showDialogModeSelector();
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
        settingsButton.setOnClickListener(onClickListener);
        mainPlayButton.setOnClickListener(onClickListener);
    }



    private void showDialogModeSelector() {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.game_mode_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
        dialog.findViewById(R.id.competitionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                final Intent intent = new Intent(HomeActivity.this, GamePlayActivity.class);
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
        dialog.findViewById(R.id.singlePlayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                // Nothing yet...
                Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
