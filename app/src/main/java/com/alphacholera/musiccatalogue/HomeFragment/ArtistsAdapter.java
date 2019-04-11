package com.alphacholera.musiccatalogue.HomeFragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.Artist;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.MyViewHolder> {

    private ArrayList<Artist> artists;
    private Context context;
    private ClickListener listener;
    private DatabaseManagement databaseManagement;

    ArtistsAdapter (Context context, ArrayList<Artist> artists, ClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.artists = artists;
        this.databaseManagement = new DatabaseManagement(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView artistImage;
        TextView artistName;
        TextView numberOfSongs;
        TextView gender;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistName = itemView.findViewById(R.id.artist_name);
            numberOfSongs = itemView.findViewById(R.id.number_of_songs);
            gender = itemView.findViewById(R.id.gender);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artist_item_recycler_view, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, myViewHolder.getAdapterPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsAdapter.MyViewHolder myViewHolder, int i) {
        Artist artist = artists.get(i);
        myViewHolder.artistName.setText(artist.getArtistName());
        Glide.with(context).load(artist.getImageURL()).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.artistImage);
        myViewHolder.gender.setText(artist.getGender());
        myViewHolder.numberOfSongs.setText(String.format("Total Songs : %s", String.valueOf(databaseManagement.getNumberOfSongsOfAnArtist(artist.getArtistID()))));
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    interface ClickListener {
        void onClick(View view, int position);
    }
}
