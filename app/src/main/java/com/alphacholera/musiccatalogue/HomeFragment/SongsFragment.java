package com.alphacholera.musiccatalogue.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.alphacholera.musiccatalogue.ViewSong;

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseManagement databaseManagement;

    private ArrayList<Song> songsList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_songs);
        databaseManagement = new DatabaseManagement(getContext());
        songsList = databaseManagement.getAllSongs();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SongsAdapter adapter = new SongsAdapter(getContext(), songsList, new SongsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), ViewSong.class);
                intent.putExtra("songID", songsList.get(position).getSongId());
                intent.putExtra("albumID", songsList.get(position).getAlbumID());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}
