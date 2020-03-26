package com.example.concentration.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.concentration.DataSave.PreferencesUtil;
import com.example.concentration.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private Button backButton;
    private ArrayList<Button> complexity = new ArrayList<>();

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

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < complexity.size(); i++) {
                    if (complexity.get(i).getId() == v.getId()) {
                        complexity.get(i).getBackground().setColorFilter(getResources().getColor(R.color.redColor), PorterDuff.Mode.MULTIPLY);
                        complexity.get(i).setTextColor(Color.WHITE);
                        PreferencesUtil.saveComplexity(SettingsActivity.this, i);
                    } else {
                        complexity.get(i).getBackground().setColorFilter(getResources().getColor(R.color.whiteColor), PorterDuff.Mode.MULTIPLY);
                        complexity.get(i).setTextColor(Color.BLACK);
                    }
                }
            }
        };

        for (int i = 0; i < complexity.size(); i++) {
            complexity.get(i).setOnClickListener(onClickListener);
        }
    }

    private void init() {
        backButton = findViewById(R.id.backButton);
        complexity.add((Button)findViewById(R.id.easyComplBut));
        complexity.add((Button)findViewById(R.id.mediumComplBut));
        complexity.add((Button)findViewById(R.id.hardComplBut));
        int index = PreferencesUtil.getComplexity(this);
        complexity.get(index).getBackground().setColorFilter(getResources().getColor(R.color.redColor), PorterDuff.Mode.MULTIPLY);
        complexity.get(index).setTextColor(Color.WHITE);
    }
}
