package com.example.concentration.Info;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post {

    public String uid;
    public String username;
    public String percents;

    public Post() { }

    public Post(String uid, String username, String percents) {
        this.uid = uid;
        this.username = username;
        this.percents = percents;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("username", username);
        result.put("percents", percents);

        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPercents() {
        return percents;
    }

    public void setPercents(String percents) {
        this.percents = percents;
    }
}
