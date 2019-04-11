package com.alphacholera.musiccatalogue;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alphacholera.musiccatalogue.Fragments.HistoryFragment;
import com.alphacholera.musiccatalogue.Fragments.ProfileFragment;
import com.alphacholera.musiccatalogue.Fragments.TrendingFragment;
import com.alphacholera.musiccatalogue.Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (getSupportActionBar() != null)
//            getSupportActionBar().hide();
        int[] icons = {R.drawable.tab_home,
                R.drawable.tab_search,
                R.drawable.tab_home,
                R.drawable.tab_search
        };

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();
        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
    }
    void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.insertNewFragment(new HomeFragment());
        adapter.insertNewFragment(new TrendingFragment());
        adapter.insertNewFragment(new HistoryFragment());
        adapter.insertNewFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
    }
}
