package com.example.concentration.Menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.CompetitionGameActivity;
import com.example.concentration.R;
import com.example.concentration.ResultsActivity;
import com.example.concentration.UnlimitedGameActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    boolean isHomeButtonPressed = false;
    int levelNumber = 1;
    final String LOG_TAG = "myLogs";
    Button rewardsButton, gameModeButton, tableOfRecordsButton, settingsButton, mainPlayButton;
    TextView lvlTextView;
    LinearLayout layout;
    private AnimationDrawable mAnimationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        layout = findViewById(R.id.main_linear);
        mAnimationDrawable = (AnimationDrawable) layout.getBackground();
        mAnimationDrawable.setEnterFadeDuration(100);
        mAnimationDrawable.setExitFadeDuration(4000);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) { isHomeButtonPressed = bundle.getBoolean("isHomeButtonPressed"); }

        rewardsButton = findViewById(R.id.rewardsButton);
        gameModeButton = findViewById(R.id.gameModeButton);
        tableOfRecordsButton = findViewById(R.id.tableOfRecordsButton);
        settingsButton = findViewById(R.id.pauseButton);
        mainPlayButton = findViewById(R.id.mainPlayButton);
        lvlTextView = findViewById(R.id.lvlTextView);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);

        readDB(); // BIG PROBLEMS WITH THIS DATA BASE!!!!!!!!!!!!!!!!!!!! FIX IT!!!

        String lvl = String.valueOf(levelNumber);
        lvlTextView.setText(lvl);

        tableOfRecordsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });

        gameModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                showDialogModeSelector();
            }
        });

        mainPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, UnlimitedGameActivity.class);
                startActivity(intent);
            }
        });

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
            }
        };
        rewardsButton.setOnClickListener(onClickListener);
        settingsButton.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAnimationDrawable.start();
    }



    private void showDialogModeSelector() {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.game_mode_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
        dialog.findViewById(R.id.competitionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                final Intent intent = new Intent(HomeActivity.this, CompetitionGameActivity.class);
                intent.putExtra("isHomButPressed", isHomeButtonPressed);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                },100);
            }
        });
        dialog.findViewById(R.id.singlePlayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                // Nothing yet...
                Toast.makeText(getApplicationContext(), "Will be available later", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void readDB() {
        SQLiteDatabase db;
        db = openOrCreateDatabase("DataBaseLevel", Context.MODE_PRIVATE, null);

        Cursor c = db.query("table_levels", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int levelColIndex = c.getColumnIndex("level");

            do {
                if (c.isLast()) levelNumber = c.getInt(levelColIndex);
                Log.d(LOG_TAG,
                        "LAST: ID = " + c.getInt(idColIndex) +
                                ", level = " + c.getInt(levelColIndex));
            } while (c.moveToNext());
        } else {
            levelNumber = 1;
            Log.d(LOG_TAG, "0 rows");
        }
    }
}
