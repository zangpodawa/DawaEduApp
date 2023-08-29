package com.dawa369.dawaeduapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dawa369.dawaeduapp.R;
import com.dawa369.dawaeduapp.model.User;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<User> scoreList;

    public ScoreAdapter(ArrayList<User> scoreList) {
        this.scoreList = scoreList;
    }

    //create new view with R.Layout.row_item as its template
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ScoreViewHolder(view);
    }

    //replace the content of an existing view with new data if applicable
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ScoreViewHolder scoreViewHolder = (ScoreViewHolder) holder;

        //get user object at the given position
        User userObj = scoreList.get(position);

        //update the scoreViewHolder
        scoreViewHolder.dateView.setText(userObj.getDate());
        scoreViewHolder.durationView.setText(userObj.getDuration());
        scoreViewHolder.levelView.setText(userObj.getLevel());
        scoreViewHolder.scoreView.setText(userObj.getScore());
        scoreViewHolder.usernameView.setText(userObj.getUsername());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    //Based on row_item define data structure of the ViewHolder
    class ScoreViewHolder extends RecyclerView.ViewHolder{

        TextView usernameView;
        TextView levelView;
        TextView scoreView;
        TextView durationView;
        TextView dateView;

        //provide a reference for the views needed to display items in scoreList
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameView = itemView.findViewById(R.id.usernameView);
            levelView = itemView.findViewById(R.id.levelView);
            scoreView = itemView.findViewById(R.id.scoreView);
            durationView = itemView.findViewById(R.id.durationView);
            dateView = itemView.findViewById(R.id.dateView);
        }
    }
}
