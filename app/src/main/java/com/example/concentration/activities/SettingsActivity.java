package com.example.concentration.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.concentration.data.SharedPreferencesUtil;
import com.example.concentration.R;

public class SettingsActivity extends AppCompatActivity {

    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        init();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void init() {
        backButton = findViewById(R.id.backButton);
        int complexity = SharedPreferencesUtil.getComplexity(this);
        int theme = SharedPreferencesUtil.getTheme(this);

        radioDefaultSelect(complexity, theme);
    }

    public void changeComplexity(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        byte chosen = -1;
        switch (view.getId()) {
            case R.id.easy_compl:
                if (checked)
                    chosen = 0;
                break;
            case R.id.normal_compl:
                if (checked)
                    chosen = 1;
                break;
            case R.id.hard_compl:
                if (checked)
                    chosen = 2;
                break;
            default:
                break;
        }
        try {
            SharedPreferencesUtil.saveComplexity(SettingsActivity.this, chosen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void radioDefaultSelect(int compl, int theme) {
        RadioButton easy = findViewById(R.id.easy_compl);
        RadioButton normal = findViewById(R.id.normal_compl);
        RadioButton hard = findViewById(R.id.hard_compl);

        RadioButton animals = findViewById(R.id.animals);
        RadioButton cars = findViewById(R.id.cars);
        RadioButton food = findViewById(R.id.food);
        RadioButton random = findViewById(R.id.everything);

        switch (compl) {
            case 0:
                easy.setChecked(true);
                break;
            case 1:
                normal.setChecked(true);
                break;
            case 2:
                hard.setChecked(true);
                break;
            default:
                break;
        }

        switch (theme) {
            case 0:
                animals.setChecked(true);
                break;
            case 1:
                cars.setChecked(true);
                break;
            case 2:
                food.setChecked(true);
                break;
            case 3:
                random.setChecked(true);
                break;
            default:
                break;
        }
    }

    public void changeTheme(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        byte chosen = -1;
        switch (view.getId()) {
            case R.id.animals:
                if (checked)
                    chosen = 0;
                break;
            case R.id.cars:
                if (checked)
                    chosen = 1;
                break;
            case R.id.food:
                if (checked)
                    chosen = 2;
                break;
            case R.id.everything:
                if (checked)
                    chosen = 3;
                break;
            default:
                break;
        }
        try {
            SharedPreferencesUtil.saveTheme(SettingsActivity.this, chosen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
