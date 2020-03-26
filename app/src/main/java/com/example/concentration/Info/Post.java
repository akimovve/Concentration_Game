package com.example.concentration.Info;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post {

    private String uid;
    private String username;
    private String percents;

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

}
