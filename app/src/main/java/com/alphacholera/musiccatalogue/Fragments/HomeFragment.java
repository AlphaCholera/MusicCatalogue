package com.alphacholera.musiccatalogue.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphacholera.musiccatalogue.HomeFragment.AlbumsFragment;
import com.alphacholera.musiccatalogue.HomeFragment.ArtistsFragment;
import com.alphacholera.musiccatalogue.HomeFragment.SongsFragment;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.ViewPagerAdapter;

public class HomeFragment extends Fragment {

    private String[] tabs = {"Songs", "Albums", "Artists"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_tab, container, false);
        TabLayout tabLayout = view.findViewById(R.id.home_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.home_view_pager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        tabLayout.getTabAt(0).select();
        for (int i = 0; i < 3; i++)
            tabLayout.getTabAt(i).setText(tabs[i]);
        return view;
    }

    void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.insertNewFragment(new SongsFragment());
        adapter.insertNewFragment(new AlbumsFragment());
        adapter.insertNewFragment(new ArtistsFragment());
        viewPager.setAdapter(adapter);
    }
}
