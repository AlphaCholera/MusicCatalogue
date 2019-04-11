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
import com.alphacholera.musiccatalogue.UtilityClasses.Album;
import com.alphacholera.musiccatalogue.ViewAlbum;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseManagement databaseManagement;

    private ArrayList<Album> albumsList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_albums);
        databaseManagement = new DatabaseManagement(getContext());
        albumsList = databaseManagement.getAllAlbums();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AlbumsAdapter adapter = new AlbumsAdapter(getContext(), albumsList, new AlbumsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), ViewAlbum.class);
                intent.putExtra("albumID", albumsList.get(position).getAlbumID());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}
