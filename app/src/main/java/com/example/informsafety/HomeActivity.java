package com.example.informsafety;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.navigation_home);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.navigation_draft_forms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DraftsFragment()).commit();
                break;
            case R.id.navigation_sent_forms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SentFormsFragment()).commit();
                break;
            case R.id.navigation_completed_forms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CompletedFormsFragment()).commit();
                break;
            case R.id.navigation_child_protection:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChildProtectionFragment()).commit();
                break;
            case R.id.navigation_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LegalFragment()).commit();
                break;
            case R.id.navigation_contact_us:
                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_privacy_policy:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LegalPrivacy()).commit();
                break;
            case R.id.navigation_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

        public void onBackPressed () {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }