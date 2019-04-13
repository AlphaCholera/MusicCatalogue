package com.alphacholera.musiccatalogue.HomeFragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.Album;
import com.alphacholera.musiccatalogue.UtilityClasses.Song;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

    public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private ArrayList<Album> albums;
    private ArrayList<Album> albumsFilteredList;
    private Context context;
    private ClickListener listener;
    private DatabaseManagement databaseManagement;

    AlbumsAdapter (Context context, ArrayList<Album> albums, ClickListener listener) {
        this.context = context;
        this.albums = albums;
        this.listener = listener;
        this.albumsFilteredList = albums;
        this.databaseManagement = new DatabaseManagement(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView albumName;
        TextView yearOfRelease;
        TextView numberOfSongs;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.albumImage = itemView.findViewById(R.id.album_image);
            this.albumName = itemView.findViewById(R.id.album_name);
            this.yearOfRelease = itemView.findViewById(R.id.year_of_release);
            this.numberOfSongs = itemView.findViewById(R.id.number_of_songs);
        }
    }

    interface ClickListener {
        void onClick(View view, Album album);
    }

    @NonNull
    @Override
    public AlbumsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.albums_item_recycler_view, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, albumsFilteredList.get(myViewHolder.getAdapterPosition()));
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsAdapter.MyViewHolder myViewHolder, int i) {
        Album album = albumsFilteredList.get(i);
        myViewHolder.albumName.setText(album.getAlbumName());
        Glide.with(context).load(album.getImageURL()).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.albumImage);
        myViewHolder.yearOfRelease.setText("Year of Release : " + String.valueOf(album.getYearOfRelease()));
        myViewHolder.numberOfSongs.setText
                (String.format("%s songs", String.valueOf(databaseManagement.getNumberOfSongsOfAnAlbum(album.getAlbumID()))));
    }

    @Override
    public int getItemCount() {
        return albumsFilteredList.size();
    }

        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        albumsFilteredList = albums;
                    } else {
                        ArrayList<Album> filteredList = new ArrayList<>();
                        for (Album album : albums) {
                            if (album.getAlbumName().toLowerCase().contains(charString.toLowerCase()))
                                filteredList.add(album);
                        }
                        albumsFilteredList = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = albumsFilteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    albumsFilteredList = (ArrayList<Album>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

}
