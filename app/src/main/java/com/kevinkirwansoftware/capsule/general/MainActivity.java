package com.kevinkirwansoftware.capsule.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.kevinkirwansoftware.capsule.fragments.FragmentGraph;
import com.kevinkirwansoftware.capsule.fragments.FragmentSchedule;
import com.kevinkirwansoftware.capsule.fragments.FragmentSettings;
import com.kevinkirwansoftware.capsule.fragments.FragmentToday;
import com.kevinkirwansoftware.capsule.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "MainActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationPreferences.loadPreferenceData(getApplicationContext());

        setTheme(R.style.ForestTheme);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentToday()).commit();
            navigationView.setCheckedItem(R.id.todayFragment);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentToday()).commit();
        navigationView.setCheckedItem(R.id.todayFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.todayFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentToday()).commit();
                break;
            case R.id.graphFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentGraph()).commit();
                break;
            case R.id.scheduleFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentSchedule()).commit();
                break;
            case R.id.settingsFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentSettings()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() called");
        ApplicationTools.scheduleJobService(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
