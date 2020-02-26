package com.example.concentration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.Menu.HomeActivity;

public class SettingsActivity extends AppCompatActivity {

    Button backButton;
    EditText numberOfColorsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        backButton = findViewById(R.id.backButton);
        numberOfColorsTextView = findViewById(R.id.numberOfColorsTextView);
        numberOfColorsTextView.setText(String.valueOf(PreferencesUtil.getNumOfColors(this)));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colors = Integer.parseInt(numberOfColorsTextView.getText().toString());
                PreferencesUtil.saveNumOfColors(SettingsActivity.this, colors);
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });


    }
}
