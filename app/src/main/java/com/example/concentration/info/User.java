package com.example.concentration.info;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public Uri urlPhoto;

    public void setUsername(String username) {
        this.username = username;
    }

    public User() { }

    public User(String username, String email, Uri urlPhoto) {
        this.username = username;
        this.email = email;
        this.urlPhoto = urlPhoto;
    }
}
