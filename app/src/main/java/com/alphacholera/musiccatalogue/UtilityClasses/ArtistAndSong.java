package com.alphacholera.musiccatalogue.UtilityClasses;

public class ArtistAndSong {
    private String songID;
    private String artistID;

    public ArtistAndSong() {

    }

    public ArtistAndSong(String songID, String artistID) {
        this.songID = songID;
        this.artistID = artistID;
    }

    public String getSongID() {
        return songID;
    }

    public String getArtistID() {
        return artistID;
    }
}
