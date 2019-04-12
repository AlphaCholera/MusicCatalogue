package com.alphacholera.musiccatalogue.UtilityClasses;

public class UserInfo {

    private String songID;
    private Long frequency;

    UserInfo() {

    }

    public UserInfo(String songID, Long frequency) {
        this.songID = songID;
        this.frequency = frequency;
    }

    public String getSongID() {
        return songID;
    }

    public Long getFrequency() {
        return frequency;
    }
}
