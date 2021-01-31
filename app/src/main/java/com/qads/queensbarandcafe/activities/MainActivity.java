package com.qads.queensbarandcafe.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.qads.queensbarandcafe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qads.queensbarandcafe.fragments.BarFragment;
import com.qads.queensbarandcafe.fragments.CafeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static List<Object> cafePrices = new ArrayList<>();
    public static List<Object> cafeCart = new ArrayList<>();
    public static List<Object> barPrices = new ArrayList<>();
    public static List<Object> barCart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CafeFragment()).commit();
        
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()){
                        case R.id.nav_cafe:
                            selectedFragment = new CafeFragment();
                            break;
                        case R.id.nav_bar:
                            selectedFragment = new BarFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };
}
