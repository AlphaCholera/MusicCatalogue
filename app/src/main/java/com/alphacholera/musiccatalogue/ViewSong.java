package com.alphacholera.musiccatalogue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class ViewSong extends AppCompatActivity {

    private DatabaseManagement databaseManagement;
    private ListView listView;
    private ImageView albumImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_song);

        databaseManagement = new DatabaseManagement(this);
        listView = findViewById(R.id.list_view);
        albumImage = findViewById(R.id.album_image);

        String songID = getIntent().getStringExtra("songID");
        String albumID = getIntent().getStringExtra("albumID");

        Glide.with(this)
                .load(databaseManagement.getAlbumURL(albumID))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(albumImage);

        String[] info = databaseManagement.getSongInfo(songID);
        ArrayList<String> details = new ArrayList<>();
        details.add("Name : "+info[1]);
        details.add("Album : " + databaseManagement.getAlbumName(albumID));
        details.add("Language : " + info[3]);

        int time = Integer.parseInt(info[4]);
        details.add("Duration : " + String.format("%02d:%02d", time/60, time%60));

        StringBuilder artistsString = new StringBuilder();
        for (String artist : databaseManagement.getArtistsOfASong(songID)) {
            artistsString.append(artist);
            artistsString.append(", ");
        }
        details.add("Artists : " + artistsString.substring(0, artistsString.length()-2));

        details.add("You have played this song " + databaseManagement.getFrequencyOfSong(songID) + " times");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, details);
        listView.setAdapter(adapter);
    }
}
