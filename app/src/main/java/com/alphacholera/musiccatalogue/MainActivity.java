package com.alphacholera.musiccatalogue;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.alphacholera.musiccatalogue.HistoryFragment.HistoryFragment;
import com.alphacholera.musiccatalogue.Fragments.ProfileFragment;
import com.alphacholera.musiccatalogue.Fragments.TrendingFragment;
import com.alphacholera.musiccatalogue.Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        actionBar = getSupportActionBar();
        loadFragment(new HomeFragment());
        actionBar.setTitle("Home");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    actionBar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.trending:
                    actionBar.setTitle("Trending");
                    fragment = new TrendingFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.history:
                    actionBar.setTitle("History");
                    fragment = new HistoryFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.profile:
                    actionBar.setTitle("Trending");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.addToBackStack(null);
        transaction.commit();
    }
}
