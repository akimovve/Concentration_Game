package com.example.concentration.Menu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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


        tableOfRecordsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
            }
        };
        rewardsButton.setOnClickListener(onClickListener);
        settingsButton.setOnClickListener(onClickListener);
        mainPlayButton.setOnClickListener(onClickListener);
        gameModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogModeSelector();
            }
        });
    }



    private void showDialogModeSelector() {
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
                Intent intent = new Intent(HomeActivity.this, GamePlayActivity.class);
                intent.putExtra("isHomButPressed", isHomeButtonPressed);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.singlePlayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nothing yet...
                Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
