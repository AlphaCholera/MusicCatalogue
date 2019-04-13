package com.alphacholera.musiccatalogue.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphacholera.musiccatalogue.HomeFragment.SongsAdapter;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.alphacholera.musiccatalogue.ViewSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TrendingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Song> songsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.trending_tab, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_trending_songs);
        songsList = new ArrayList<>();
        getMostPopularSongs(new DataStatus() {
            @Override
            public void readData() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                SongsAdapter adapter = new SongsAdapter(getContext(), songsList, new SongsAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, Song song) {
                        Intent intent = new Intent(getContext(), ViewSong.class);
                        intent.putExtra("songID", song.getSongId());
                        intent.putExtra("albumID", song.getAlbumID());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    public void getMostPopularSongs (final DataStatus status) {
        Query query = FirebaseDatabase.getInstance().getReference().child("songs").orderByChild("frequency");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Song song = snapshot.getValue(Song.class);
                    songsList.add(song);
                }
                Collections.sort(songsList, new Comparator<Song>() {
                    @Override
                    public int compare(Song song1, Song song2) {
                        return song2.getFrequency() - song1.getFrequency();
                    }
                });
                status.readData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    interface DataStatus {
        public void readData();
    }

}
