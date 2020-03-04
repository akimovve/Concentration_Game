package com.example.concentration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.DataSave.PreferencesUtil;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private Button backButton;
    private TextView difTextView;
    private SeekBar colorsSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        init();
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
                difTextView.setText("Complexity level: " + (PreferencesUtil.getComplexity(SettingsActivity.this) + 1));
                colorsSeekBar.setProgress(PreferencesUtil.getComplexity(SettingsActivity.this));
            }
        });

    }

    private void init() {
        backButton = findViewById(R.id.backButton);
        difTextView = findViewById(R.id.difTextView);
        colorsSeekBar = findViewById(R.id.colorsSeekBar);
        colorsSeekBar.setOnSeekBarChangeListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int difficultyLevel = colorsSeekBar.getProgress();
        PreferencesUtil.saveComplexity(SettingsActivity.this, difficultyLevel);
        difTextView.setText("Complexity level: " + (PreferencesUtil.getComplexity(this) + 1));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
