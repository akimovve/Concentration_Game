package com.example.concentration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.Menu.HomeActivity;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    Button backButton;
    TextView difTextView;
    SeekBar colorsSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        backButton = findViewById(R.id.backButton);
        difTextView = findViewById(R.id.difTextView);
        colorsSeekBar = findViewById(R.id.colorsSeekBar);
        colorsSeekBar.setOnSeekBarChangeListener(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });

        colorsSeekBar.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                difTextView.setText("Complexity level: " + (PreferencesUtil.getNumOfColors(SettingsActivity.this) + 1));
                colorsSeekBar.setProgress(PreferencesUtil.getNumOfColors(SettingsActivity.this));
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int difficultyLevel = colorsSeekBar.getProgress();
        PreferencesUtil.saveNumOfColors(SettingsActivity.this, difficultyLevel);
        difTextView.setText("Complexity level: " + (PreferencesUtil.getNumOfColors(this) + 1));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
