package com.example.concentration.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.fragments.NetworkFragment;
import com.example.concentration.fragments.PhoneFragment;
import com.example.concentration.fragments.ProfileFragment;
import com.example.concentration.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class InfoActivity extends AppCompatActivity {

    private static final String LOG_TAG = InfoActivity.class.getSimpleName();
    Button backBut;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.info_layout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        boolean sign = false;
        if (bundle != null) {
            sign = bundle.getBoolean("sign_up");
        }

        if (sign) {
            Log.d(LOG_TAG, "signed in");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PhoneFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            Log.d(LOG_TAG, "not signed in");
            if (savedInstanceState == null) { // for not to delete data with rotation of device
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PhoneFragment()).commit();
                bottomNavigationView.setSelectedItemId(R.id.nav_phone);
            }
        }

        backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(InfoActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_phone:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new PhoneFragment()).commit();
                            break;
                        case R.id.nav_network:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new NetworkFragment()).commit();
                            break;
                        case R.id.nav_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new ProfileFragment()).commit();
                            break;
                    }
                    return true;
                }
            };
}