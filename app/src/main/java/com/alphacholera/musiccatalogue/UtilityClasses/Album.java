package com.alphacholera.musiccatalogue.UtilityClasses;

public class Album {
    private String albumID;
    private String albumName;
    private String imageURL;
    private int yearOfRelease;

    // Constructor invoked when data loaded from FireBase
    public Album() {

    }

    public Album(String albumID, String albumName, String imageURL, int yearOfRelease) {
        this.albumID = albumID;
        this.albumName = albumName;
        this.imageURL = imageURL;
        this.yearOfRelease = yearOfRelease;
    }

    public String getAlbumID() {
        return albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }
}
