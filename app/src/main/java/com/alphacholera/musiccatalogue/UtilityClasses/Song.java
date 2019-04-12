package com.alphacholera.musiccatalogue.UtilityClasses;

public class Song {
    private String songId;
    private String songName;
    private String albumID;
    private String language;
    private int duration;
    private int frequency;

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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
