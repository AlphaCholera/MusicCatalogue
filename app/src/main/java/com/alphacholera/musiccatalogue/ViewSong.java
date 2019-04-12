package com.alphacholera.musiccatalogue;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewSong extends AppCompatActivity {

    private DatabaseManagement databaseManagement;
    private ListView listView;
    private ImageView albumImage;
    private Button playSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_song);

        databaseManagement = new DatabaseManagement(this);
        listView = findViewById(R.id.list_view);
        albumImage = findViewById(R.id.album_image);
        playSong = findViewById(R.id.play_song);

        final String songID = getIntent().getStringExtra("songID");
        String albumID = getIntent().getStringExtra("albumID");

        Glide.with(this)
                .load(databaseManagement.getAlbumURL(albumID))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(albumImage);

        String[] info = databaseManagement.getSongInfo(songID);
        final ArrayList<String> details = new ArrayList<>();
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

        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int frequency = databaseManagement.updateUserData(songID);

                FirebaseDatabase.getInstance().getReference()
                        .child("userInfo")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("songInfo")
                        .child(songID)
                        .setValue(frequency);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                FirebaseDatabase.getInstance().getReference()
                        .child("userInfo")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("historyInfo")
                        .child(time)
                        .setValue(songID);

                final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("songs").child(songID);
                dbref.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Song song = mutableData.getValue(Song.class);
                        int frequency = song.getFrequency();
                        song.setFrequency(frequency+1);
                        dbref.setValue(song);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    }
                });

                databaseManagement.insertIntoHistoryTable(songID, time);
                details.remove(5);
                details.add(5, "You have played this song " + frequency + " times");
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "You have played this song..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
