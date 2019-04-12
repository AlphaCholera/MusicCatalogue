package com.alphacholera.musiccatalogue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.alphacholera.musiccatalogue.UtilityClasses.Album;
import com.alphacholera.musiccatalogue.UtilityClasses.Artist;
import com.alphacholera.musiccatalogue.UtilityClasses.ArtistAndSong;
import com.alphacholera.musiccatalogue.UtilityClasses.History;
import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.alphacholera.musiccatalogue.UtilityClasses.User;
import com.alphacholera.musiccatalogue.UtilityClasses.UserInfo;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    // For FireBase Authentication
    private static final int RC_SIGN_IN = 1;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    // For knowing whether the user has opened the app for the first time
    private SharedPreferences sharedPref;

    // For collecting all the data from the FireBase
    private FirebaseDatabase firebaseDatabase;
    // For storing the data into local database
    private DatabaseManagement databaseManagement;

    // Store all the data into ArrayLists
    private ArrayList<Song> songsList = new ArrayList<>();
    private ArrayList<Album> albumsList = new ArrayList<>();
    private ArrayList<Artist> artistsList = new ArrayList<>();
    private ArrayList<ArtistAndSong> compositionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        // Initializing objects of the respective classes
        databaseManagement = new DatabaseManagement(this);
        sharedPref = getSharedPreferences("com.alphacholera.musiccatalogue", MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();

        // If the user has logged in for the first time, copy all data from FireBase into the local database
        if (sharedPref.getBoolean("firstRun", true)) {
            Toast.makeText(getApplicationContext(), "First time opening app", Toast.LENGTH_SHORT).show();

            readSongs(new DataStatus() {
                @Override
                public void readAllData() {
                    databaseManagement.addAllDataIntoTables(songsList, albumsList, artistsList, compositionsList);
                    sharedPref.edit().putBoolean("firstRun", false).apply();
                }
            });
        }

        // Initializing the authentication components of FireBase Auth
        firebaseAuth = FirebaseAuth.getInstance();


        // If no user is registered in the app, make him/her register
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // Not signed in
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                            new AuthUI.IdpConfig.EmailBuilder().build()
                    ))
                    .build(), RC_SIGN_IN);

        } else {
            // User already has an account
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void readSongs(final DataStatus status) {
        final DatabaseReference dbref = firebaseDatabase.getReference();
        dbref.keepSynced(true);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                songsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.child("songs").getChildren()) {
                    Song song = snapshot.getValue(Song.class);
                    songsList.add(song);
                }
                for (DataSnapshot snapshot : dataSnapshot.child("albums").getChildren()) {
                    Album album = snapshot.getValue(Album.class);
                    albumsList.add(album);
                }
                for (DataSnapshot snapshot : dataSnapshot.child("artists").getChildren()) {
                    Artist artist = snapshot.getValue(Artist.class);
                    artistsList.add(artist);
                }
                for (DataSnapshot snapshot : dataSnapshot.child("composition").getChildren()) {
                    ArtistAndSong composition = snapshot.getValue(ArtistAndSong.class);
                    compositionsList.add(composition);
                }
                dbref.removeEventListener(this);
                status.readAllData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // User has signed in for the first time
                currentUser = firebaseAuth.getCurrentUser();
                firebaseDatabase.getReference()
                        .child("users")
                        .child(currentUser.getUid())
                        .setValue(new User(currentUser.getUid(), currentUser.getDisplayName(),
                                currentUser.getEmail(), currentUser.getPhotoUrl().toString()));
                Toast.makeText(getApplicationContext(), "Writing user data to FireBase", Toast.LENGTH_SHORT).show();
                readUserInfo(new UserDataStatus() {
                    @Override
                    public void readAllData() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    void readUserInfo(final UserDataStatus status) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference dbref = firebaseDatabase.getReference().child("userInfo").child(currentUser.getUid());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.child("songInfo").getChildren()) {
                        UserInfo userInfo = new UserInfo(snapshot.getKey(), (Long) snapshot.getValue());
                        databaseManagement.insertIntoUserTable(userInfo.getFrequency(), userInfo.getSongID());
                    }
                    for (DataSnapshot snapshot : dataSnapshot.child("historyInfo").getChildren()) {
                        History history = new History((String)snapshot.getValue(), snapshot.getKey());
                        databaseManagement.insertIntoHistoryTable(history.getSongID(), history.getDateAndTime());
                    }
                dbref.removeEventListener(this);
                status.readAllData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface DataStatus {
        void readAllData();
    }

    public interface UserDataStatus {
        void readAllData();
    }
}
