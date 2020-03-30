package com.example.concentration.Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.concentration.R;

import java.util.List;

public class RecyclerView_Config {

    private Context mContext;
    private UsersAdapter mUsersAdapter;


    public void setConfig(RecyclerView recyclerView, Context context, List<Post> users, List<String> keys) {
        mContext = context;
        mUsersAdapter = new UsersAdapter(users, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mUsersAdapter);
    }

    class UserItemView extends RecyclerView.ViewHolder {
        private TextView mUsername;
        private TextView mPercents;

        private String key;

        public UserItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.user_list_item, parent, false));

            mUsername = itemView.findViewById(R.id.username_txtView);
            mPercents = itemView.findViewById(R.id.percents_txtView);
        }

        public void bind(Post post, String key) {
            mUsername.setText(post.getUsername());
            mPercents.setText(post.getPercents());
            this.key = key;

        }
    }

    class UsersAdapter extends RecyclerView.Adapter<UserItemView> {
        private List<Post> mUserList;
        private List<String> mKeys;

        public UsersAdapter(List<Post> mUserList, List<String> mKeys) {
            this.mUserList = mUserList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public UserItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UserItemView holder, int position) {
            holder.bind(mUserList.get(position), mKeys.get(position));

        }

        @Override
        public int getItemCount() {
            return mUserList.size();
        }
    }
}
