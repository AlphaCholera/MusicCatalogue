package com.alphacholera.musiccatalogue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class ViewAlbum extends AppCompatActivity {

    private DatabaseManagement databaseManagement;
    private ListView albumInfoListView, songsListView;
    private ImageView albumImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);

        databaseManagement = new DatabaseManagement(this);
        albumInfoListView = findViewById(R.id.album_info_list_view);
        songsListView = findViewById(R.id.songs_info_list_view);
        albumImage = findViewById(R.id.album_image);

        String albumID = getIntent().getStringExtra("albumID");

        Glide.with(this)
                .load(databaseManagement.getAlbumURL(albumID))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(albumImage);

        String[] info = databaseManagement.getAlbumInfo(albumID);
        ArrayList<String> details = new ArrayList<>();
        details.add("Album : " + info[1]);
        details.add("Year of Release : " + info[3]);
        details.add("Number of Songs : " + databaseManagement.getNumberOfSongsOfAnAlbum(albumID));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, details);
        albumInfoListView.setAdapter(adapter1);

        final ArrayList<Song> songs = databaseManagement.getSongsOfAnAlbum(albumID);
        ArrayList<String> songNames = new ArrayList<>();
        for (Song song : songs)
            songNames.add(song.getSongName());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songNames);
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewAlbum.this, ViewSong.class);
                intent.putExtra("songID", songs.get(position).getSongId());
                intent.putExtra("albumID", songs.get(position).getAlbumID());
                startActivity(intent);
            }
        });
        songsListView.setAdapter(adapter2);
    }
}
