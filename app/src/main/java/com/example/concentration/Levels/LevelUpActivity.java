package com.example.concentration.Levels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.concentration.Info.Variables;
import com.example.concentration.GamePlayActivity;
import com.example.concentration.R;


public class LevelUpActivity extends Activity {

    Button restartButton, nextButton;
    Intent intent;
    Variables var;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelup_layout);

        restartButton = findViewById(R.id.restartButton);
        nextButton = findViewById(R.id.nextButton);


        OnClickListener onButtonClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if (v.getId() == R.id.restartButton) flag = false;
                var = new Variables(flag); // check if the user tapped "next" or "previous" for changing level number and delay
                intent = new Intent(LevelUpActivity.this, GamePlayActivity.class);
                intent.putExtra("whichLevel", var.changeDelay);
                intent.putExtra("levelUp",flag);

                startActivity(intent);
            }
        };

        restartButton.setOnClickListener(onButtonClick);
        nextButton.setOnClickListener(onButtonClick);
    }
}
