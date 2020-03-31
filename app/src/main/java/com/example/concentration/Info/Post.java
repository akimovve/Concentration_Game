package com.example.concentration.Info;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post implements Comparable<Post> {

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

    @Override
    public int compareTo(@NonNull Post o) {
        if (getPercents() == null || o.getPercents() == null) {
            return 0;
        }
        Double first = Double.parseDouble(getPercents());
        Double second = Double.parseDouble(o.getPercents());
        return second.compareTo(first);
    }

    public String getUsername() {
        return username;
    }

    public String getPercents() {
        return percents;
    }

}
