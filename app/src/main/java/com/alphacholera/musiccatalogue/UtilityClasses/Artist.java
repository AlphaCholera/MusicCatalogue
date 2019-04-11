package com.alphacholera.musiccatalogue.UtilityClasses;

public class Artist {
    private String artistID;
    private String artistName;
    private String gender;
    private String imageURL;

    // Constructor invoked when data loaded from FireBase
    public Artist() {

    }

    public Artist(String artistID, String artistName, String gender, String imageURL) {
        this.artistID = artistID;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.gender = gender;
    }

    public String getArtistID() {
        return artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getGender() {
        return gender;
    }
}
