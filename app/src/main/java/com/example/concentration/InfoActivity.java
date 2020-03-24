package com.example.concentration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.concentration.Fragments.NetworkFragment;
import com.example.concentration.Fragments.PhoneFragment;
import com.example.concentration.Fragments.ProfileFragment;
import com.example.concentration.Fragments.SignInFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class InfoActivity extends AppCompatActivity {

    Button backBut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.results_data_layout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) { // for not to delete data with rotation of device
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PhoneFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_phone);
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
                                    new SignInFragment()).commit();
                            break;
                        case R.id.nav_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new SignInFragment()).commit();
                            break;
                    }

                    return true;
                }
            };
}
