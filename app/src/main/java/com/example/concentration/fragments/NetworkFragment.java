package com.example.concentration.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.concentration.info.Post;
import com.example.concentration.info.RecyclerView_Config;
import com.example.concentration.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkFragment extends Fragment {

    private static final String LOG_TAG = NetworkFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<Post> usersInfo = new ArrayList<>();


    public interface DataStatus {
        void DataIsLoaded(List<Post> usersInfo, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_users);

        mDatabase = FirebaseDatabase.getInstance().getReference("posts");

        readUsers(new DataStatus() {
            @Override
            public void DataIsLoaded(List<Post> usersInfo, List<String> keys) {
                Collections.sort(usersInfo);

                Map<Post, Integer> positions = new HashMap<>();
                int increment = 1;
                for (Post p : usersInfo) {
                    positions.put(p, increment++);
                }

                new RecyclerView_Config().setConfig(mRecyclerView, getActivity(), usersInfo, keys, positions);
            }

            @Override
            public void DataIsInserted() { }

            @Override
            public void DataIsUpdated() { }

            @Override
            public void DataIsDeleted() { }
        });
        return view;
    }

    private void readUsers(final DataStatus dataStatus) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersInfo.clear();

                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Post post = keyNode.getValue(Post.class);
                    usersInfo.add(post);
                }

                dataStatus.DataIsLoaded(usersInfo, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
