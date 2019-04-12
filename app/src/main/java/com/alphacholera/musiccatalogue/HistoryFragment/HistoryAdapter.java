package com.alphacholera.musiccatalogue.HistoryFragment;

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
import com.alphacholera.musiccatalogue.UtilityClasses.History;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<History> histories;
    private Context context;
    private DatabaseManagement databaseManagement;
    private ClickListener listener;

    HistoryAdapter (Context context, ArrayList<History> histories, ClickListener listener) {
        this.context = context;
        this.histories = histories;
        this.listener = listener;
        this.databaseManagement = new DatabaseManagement(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView time;
        TextView songName;
        TextView albumName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.song_image_history);
            time = itemView.findViewById(R.id.time_history);
            songName = itemView.findViewById(R.id.song_name_history);
            albumName = itemView.findViewById(R.id.album_name_history);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item_recycler_view, viewGroup, false);
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
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder myViewHolder, int i) {
        History history = histories.get(i);
        String[] songInfo = databaseManagement.getSongInfo(history.getSongID());
        String[] albumInfo = databaseManagement.getAlbumInfo(songInfo[2]);
        myViewHolder.albumName.setText(albumInfo[1]);
        Glide.with(context).load(albumInfo[2]).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.songImage);
        myViewHolder.songName.setText(songInfo[1]);
        myViewHolder.time.setText(history.getDateAndTime());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    interface ClickListener {
        public void onClick(View view, int position);
    }
}
