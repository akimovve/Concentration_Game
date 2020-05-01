package com.example.concentration.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String urlPhoto;

    public void setUsername(String username) {
        this.username = username;
    }

    public User() { }

    public User(String username, String email, String urlPhoto) {
        this.username = username;
        this.email = email;
        this.urlPhoto = urlPhoto;
    }
}
