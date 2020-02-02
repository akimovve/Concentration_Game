package com.example.concentration.SettingsMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.MainActivity;
import com.example.concentration.R;

public class Settings extends AppCompatActivity {

    Button resumeButton, newGameButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        resumeButton = findViewById(R.id.resumeButton);
        newGameButton = findViewById(R.id.newGameButton);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
