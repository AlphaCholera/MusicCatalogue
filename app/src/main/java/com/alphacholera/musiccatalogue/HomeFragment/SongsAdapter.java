package com.alphacholera.musiccatalogue.HomeFragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.Album;
import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {

    private ArrayList<Song> songs;
    private Context context;
    private ClickListener listener;
    private DatabaseManagement databaseManagement;

    SongsAdapter(Context context, ArrayList<Song> songs, ClickListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
        this.databaseManagement = new DatabaseManagement(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView songName;
        TextView albumName;
        TextView duration;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.song_image);
            this.songName = itemView.findViewById(R.id.song_name);
            this.albumName = itemView.findViewById(R.id.album_name);
            this.duration = itemView.findViewById(R.id.duration);
        }
    }

    @NonNull
    @Override
    public SongsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_item_recycler_view, viewGroup, false);
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
    public void onBindViewHolder(@NonNull SongsAdapter.MyViewHolder myViewHolder, int i) {
        Song song = songs.get(i);
        myViewHolder.songName.setText(song.getSongName());
        myViewHolder.albumName.setText(databaseManagement.getAlbumName(song.getAlbumID()));
        String image = databaseManagement.getAlbumURL(song.getAlbumID());
        Glide.with(context).load(image).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.image);
        int time = song.getDuration();
        myViewHolder.duration.setText(String.format("%02d:%02d", time/60, time%60));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    interface ClickListener {
        public void onClick(View view, int position);
    }
}
