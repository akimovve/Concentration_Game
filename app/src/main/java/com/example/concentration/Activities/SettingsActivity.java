package com.example.concentration.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.concentration.DataSave.SharedPreferencesUtil;
import com.example.concentration.R;

public class SettingsActivity extends AppCompatActivity {

    private Button backButton;

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
    }

    private void init() {
        backButton = findViewById(R.id.backButton);
        int index = SharedPreferencesUtil.getComplexity(this);
        radioDefaultSelect(index);
    }

    public void onRadioButtonClicked(View view) {
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

    private void radioDefaultSelect(int i) {
        RadioButton easy = findViewById(R.id.easy_compl);
        RadioButton normal = findViewById(R.id.normal_compl);
        RadioButton hard = findViewById(R.id.hard_compl);

        switch (i) {
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
    }
}
