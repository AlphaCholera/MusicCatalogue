package com.alphacholera.musiccatalogue.UtilityClasses;

public class Song {
    private String songId;
    private String songName;
    private String albumID;
    private String language;
    private int duration;

    // Constructor invoked when data loaded from FireBase
    public Song() {

    }

    public Song(String songId, String songName, String albumID, String language, int duration) {
        this.songId = songId;
        this.songName = songName;
        this.albumID = albumID;
        this.language = language;
        this.duration = duration;
    }

    public String getSongId() {
        return songId;
    }

    public String getSongName() {
        return songName;
    }

    public String getAlbumID() {
        return albumID;
    }

    public String getLanguage() {
        return language;
    }

    public int getDuration() {
        return duration;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
