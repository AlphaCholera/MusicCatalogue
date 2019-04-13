package com.alphacholera.musiccatalogue.HomeFragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.Album;
import com.alphacholera.musiccatalogue.ViewAlbum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseManagement databaseManagement;
    private AlbumsAdapter adapter;
    private ArrayList<Album> albumsList;
    private SearchView searchView;
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
        setHasOptionsMenu(true);
        Collections.sort(albumsList, new Comparator<Album>() {
            @Override
            public int compare(Album album1, Album album2) {
                return album1.getAlbumName().compareTo(album2.getAlbumName());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlbumsAdapter(getContext(), albumsList, new AlbumsAdapter.ClickListener() {
            @Override
            public void onClick(View view, Album album) {
                Intent intent = new Intent(getContext(), ViewAlbum.class);
                intent.putExtra("albumID", album.getAlbumID());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search)
            return true;
        return super.onOptionsItemSelected(item);
    }
}
