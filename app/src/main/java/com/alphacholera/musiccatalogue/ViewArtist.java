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

public class ViewArtist extends AppCompatActivity {

    private DatabaseManagement databaseManagement;
    private ListView artistInfoListView, songsListView;
    private ImageView artistImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artist);

        databaseManagement = new DatabaseManagement(this);
        artistInfoListView = findViewById(R.id.artist_info_list_view);
        songsListView = findViewById(R.id.songs_info_list_view);
        artistImage = findViewById(R.id.artist_image);

        String artistID = getIntent().getStringExtra("artistID");
        String[] info = databaseManagement.getArtistInfo(artistID);

        Glide.with(this)
                .load(info[3])
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(artistImage);
        ArrayList<String> details = new ArrayList<>();
        details.add("Artist Name : " + info[1]);
        details.add("Gender : " + info[2]);
        details.add("Number of Songs : " + databaseManagement.getNumberOfSongsOfAnArtist(artistID));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, details);
        artistInfoListView.setAdapter(adapter1);

        final ArrayList<Song> songs = databaseManagement.getSongsOfAnArtist(artistID);
        ArrayList<String> songNames = new ArrayList<>();
        for (Song song : songs)
            songNames.add(song.getSongName());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songNames);
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewArtist.this, ViewSong.class);
                intent.putExtra("songID", songs.get(position).getSongId());
                intent.putExtra("albumID", songs.get(position).getAlbumID());
                startActivity(intent);
            }
        });
        songsListView.setAdapter(adapter2);
    }
}
