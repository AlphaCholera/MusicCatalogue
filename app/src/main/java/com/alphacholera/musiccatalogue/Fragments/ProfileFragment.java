package com.alphacholera.musiccatalogue.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private ListView listView;
    private FirebaseUser firebaseUser;
    private ArrayList<String> strings;
    private Button signOut, refreshAllData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_tab, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        listView = view.findViewById(R.id.profile_list_view);
        signOut = view.findViewById(R.id.sign_out);
        refreshAllData = view.findViewById(R.id.refresh_all_data);

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
                new DatabaseManagement(getContext()).deleteAllTables();
                AuthUI.getInstance().signOut(getContext());
                getActivity().finish();
            }
        });

        refreshAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

}
