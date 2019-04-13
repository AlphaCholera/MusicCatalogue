package com.alphacholera.musiccatalogue.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.SplashActivity;
import com.alphacholera.musiccatalogue.UtilityClasses.Album;
import com.alphacholera.musiccatalogue.UtilityClasses.Artist;
import com.alphacholera.musiccatalogue.UtilityClasses.ArtistAndSong;
import com.alphacholera.musiccatalogue.UtilityClasses.History;
import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.alphacholera.musiccatalogue.UtilityClasses.UserInfo;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private ListView listView;
    private FirebaseUser firebaseUser;
    private ArrayList<String> strings;
    private Button signOut, refreshAllData;
    private DatabaseManagement databaseManagement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_tab, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        listView = view.findViewById(R.id.profile_list_view);
        signOut = view.findViewById(R.id.sign_out);
        refreshAllData = view.findViewById(R.id.refresh_all_data);
        databaseManagement = new DatabaseManagement(getContext());
        strings = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Glide.with(getContext()).load(Objects.requireNonNull(firebaseUser.getPhotoUrl())).into(profileImage);
        strings.add("Display Name : " + firebaseUser.getDisplayName());
        strings.add("Email Address : " + firebaseUser.getEmail());
        if (firebaseUser.getPhoneNumber() != null)
            strings.add("Phone Number : " + firebaseUser.getPhoneNumber());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref2 = getContext().getSharedPreferences("com.alphacholera.musiccatalogue", MODE_PRIVATE);
                sharedPref2.edit().putBoolean("userDataFetch", true).apply();
                // databaseManagement.deleteAllTables();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), SplashActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        refreshAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFreshData(new DataStatus() {
                    @Override
                    public void readAllData() {
                        Toast.makeText(getContext(), "Data refreshed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    void readFreshData(final DataStatus status) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        dbref.keepSynced(true);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Song> songsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("songs").getChildren()) {
                    Song song = snapshot.getValue(Song.class);
                    songsList.add(song);
                }
                ArrayList<Album> albumsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("albums").getChildren()) {
                    Album album = snapshot.getValue(Album.class);
                    albumsList.add(album);
                }
                ArrayList<Artist> artistsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("artists").getChildren()) {
                    Artist artist = snapshot.getValue(Artist.class);
                    artistsList.add(artist);
                }
                ArrayList<ArtistAndSong> compositionsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("composition").getChildren()) {
                    ArtistAndSong composition = snapshot.getValue(ArtistAndSong.class);
                    compositionsList.add(composition);
                }
                databaseManagement.deleteAllTables();
                databaseManagement.addAllDataIntoTables(songsList, albumsList, artistsList, compositionsList);
                for (DataSnapshot snapshot : dataSnapshot.child("userInfo").child(firebaseUser.getUid()).child("songInfo").getChildren()) {
                    UserInfo userInfo = new UserInfo(snapshot.getKey(), (Long) snapshot.getValue());
                    databaseManagement.insertIntoUserTable(userInfo.getFrequency(), userInfo.getSongID());
                }
                for (DataSnapshot snapshot : dataSnapshot.child("userInfo").child(firebaseUser.getUid()).child("historyInfo").getChildren()) {
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

    interface DataStatus {
        void readAllData();
    }
}
