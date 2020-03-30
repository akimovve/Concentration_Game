package com.example.concentration.Info;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public Uri urlPhoto;


    public User() { }

    public User(String username, String email, Uri urlPhoto) {
        this.username = username;
        this.email = email;
        this.urlPhoto = urlPhoto;
    }
}
