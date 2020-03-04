package com.example.concentration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.concentration.Game.ChallengeGameActivity;
import com.example.concentration.Game.MajorGameActivity;
import com.example.concentration.Info.Variables;

public class PauseActivity extends AppCompatActivity {

    private Button resumeButton, newGameButton, homeButton;
    Variables var;
    private boolean whichActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause_layout);
        whichActivity = false;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            whichActivity = bundle.getBoolean("activity");

        init();
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                var = new Variables(false);
                Intent intent;
                if (whichActivity) {
                    intent = new Intent(PauseActivity.this, MajorGameActivity.class);
                } else intent = new Intent(PauseActivity.this, ChallengeGameActivity.class);
                intent.putExtra("levelUp",false);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(PauseActivity.this, HomeActivity.class);
                intent.putExtra("homeButtonIsPressed", true);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });
    }

    private void init() {
        resumeButton = findViewById(R.id.resumeButton);
        newGameButton = findViewById(R.id.newGameButton);
        homeButton = findViewById(R.id.homeButton);
    }
}
