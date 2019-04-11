package com.alphacholera.musiccatalogue.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.Artist;
import com.alphacholera.musiccatalogue.ViewArtist;
import com.alphacholera.musiccatalogue.ViewSong;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseManagement databaseManagement;

    private ArrayList<Artist> artistsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_artists);
        databaseManagement = new DatabaseManagement(getContext());
        artistsList = databaseManagement.getAllArtists();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArtistsAdapter adapter = new ArtistsAdapter(getContext(), artistsList, new ArtistsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), ViewArtist.class);
                intent.putExtra("artistID", artistsList.get(position).getArtistID());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}
