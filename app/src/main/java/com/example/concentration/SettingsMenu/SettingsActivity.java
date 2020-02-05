package com.example.concentration.SettingsMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.Info.Variables;
import com.example.concentration.GamePlayActivity;
import com.example.concentration.R;

public class SettingsActivity extends AppCompatActivity {

    Button resumeButton, newGameButton;
    Variables var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause_layout);

        resumeButton = findViewById(R.id.resumeButton);
        newGameButton = findViewById(R.id.newGameButton);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var = new Variables(false);
                Intent intent = new Intent(SettingsActivity.this, GamePlayActivity.class);
                intent.putExtra("levelUp",false);
                startActivity(intent);
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
