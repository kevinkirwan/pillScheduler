package com.kevinkirwansoftware.capsule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            case R.id.itemB:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentInventory()).commit();
                break;
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

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}
