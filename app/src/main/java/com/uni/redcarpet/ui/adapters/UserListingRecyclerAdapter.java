package com.uni.redcarpet.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uni.redcarpet.R;
import com.uni.redcarpet.models.User;


import java.util.List;

/**
 * Created by cyrille on 26/11/17.
 */



public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;

    public UserListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);

        if (user.getPhoneNumber().equals("") & !user.getEmail().equals("")){
            String alphabet = user.getEmail().substring(0, 1);

            holder.txtUsername.setText(user.getEmail());
            holder.txtUserAlphabet.setText(alphabet);
        }else
        {
            String alphabet = "U";

            holder.txtUsername.setText(user.getPhoneNumber());
            holder.txtUserAlphabet.setText(alphabet);

        }
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
        }
    }
}

