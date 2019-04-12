package com.alphacholera.musiccatalogue.UtilityClasses;

public class History {
    private String songID;
    private String dateAndTime;

    public History(String songID, String dateAndTime) {
        this.songID = songID;
        this.dateAndTime = dateAndTime;
    }

    public String getSongID() {
        return songID;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }
}
