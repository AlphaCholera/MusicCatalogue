package com.alphacholera.musiccatalogue.UtilityClasses;

public class User {
    private String uid;
    private String name;
    private String emailID;
    private String photoURL;

    public User(String uid, String name, String emailID, String photoURL) {
        this.uid = uid;
        this.name = name;
        this.emailID = emailID;
        this.photoURL = photoURL;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
