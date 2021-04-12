package com.kevinkirwansoftware.capsule.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.kevinkirwansoftware.capsule.ReminderCheckJobService;
import com.kevinkirwansoftware.capsule.fragments.FragmentInventory;
import com.kevinkirwansoftware.capsule.fragments.FragmentSchedule;
import com.kevinkirwansoftware.capsule.fragments.FragmentSettings;
import com.kevinkirwansoftware.capsule.fragments.FragmentToday;
import com.kevinkirwansoftware.capsule.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "MainActivity.java";
    private static final int SERVICE_ID = 444;

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
            navigationView.setCheckedItem(R.id.itemA);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentToday()).commit();
        navigationView.setCheckedItem(R.id.itemA);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.itemA:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentToday()).commit();
                break;
                /*
            case R.id.itemB:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentInventory()).commit();
                break;

                 */
            case R.id.itemC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentSchedule()).commit();
                break;
            case R.id.itemD:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentSettings()).commit();
                break;
                /*
            case R.id.single_item_a:
                Toast.makeText(getApplicationContext(), "Single Item A Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.single_item_b:
                Toast.makeText(getApplicationContext(), "Single Item B Selected", Toast.LENGTH_SHORT).show();
                break;

                 */
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void scheduleJob(){
        ComponentName componentName = new ComponentName(this, ReminderCheckJobService.class);
        JobInfo info = new JobInfo.Builder(SERVICE_ID, componentName)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "ReminderCheckJobService started...");
        } else {
            Log.d(TAG, "ReminderCheckJobService failed.");
        }
    }

    private void cancelJob(){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(SERVICE_ID);
        Log.d(TAG, "Job cancelled.");
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
        scheduleJob();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
