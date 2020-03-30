package com.example.concentration.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.Fragments.NetworkFragment;
import com.example.concentration.Fragments.PhoneFragment;
import com.example.concentration.Fragments.ProfileFragment;
import com.example.concentration.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InfoActivity extends AppCompatActivity {

    Button backBut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.results_data_layout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        Bundle bundle = getIntent().getExtras();
        boolean sign = false;
        if (bundle != null) {
            sign = bundle.getBoolean("sign_up");
        }

        if (sign) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PhoneFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
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
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
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
